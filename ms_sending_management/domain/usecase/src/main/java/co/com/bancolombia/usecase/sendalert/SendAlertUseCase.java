package co.com.bancolombia.usecase.sendalert;

import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.alert.gateways.AlertGateway;
import co.com.bancolombia.model.alertclient.AlertClient;
import co.com.bancolombia.model.alertclient.gateways.AlertClientGateway;
import co.com.bancolombia.model.alerttransaction.AlertTransaction;
import co.com.bancolombia.model.alerttransaction.gateways.AlertTransactionGateway;
import co.com.bancolombia.model.client.gateways.ClientGateway;
import co.com.bancolombia.model.message.Message;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class SendAlertUseCase {
    private final AlertTransactionGateway alertTransactionGateway;
    private final AlertClientGateway alertClientGateway;
    private final ClientGateway clientGateway;
    private final AlertGateway alertGateway;

    private Function<Message, Mono<Void>> getFunction(Integer idOperation){
        final Map<Integer, Function<Message, Mono<Void>>> functions = new HashMap<>();
        functions.put(0, this::validateWithDataClient);
        functions.put(1, this::validateBasic);
        functions.put(2, this::validateWithCodeTrx);
        functions.put(3, this::validateOthersChannels);
        functions.put(4, this::validateOthersChannels);
        functions.put(5, this::validateWithCodeAlert);

        return functions.get(idOperation);
    }

    private final Predicate<Message> isValidMailAndMobile = message ->
            (Objects.nonNull(message.getMail()) && !message.getMail().isEmpty())
            || (Objects.nonNull(message.getMobile()) && !message.getMobile().isEmpty());

    private final Predicate<String> isValidFormatMail = email ->
            Pattern.compile("^(([0-9a-zA-Z]+[-._+&])*[0-9a-zA-Z]+)+@([-0-9a-zA-Z]+[.])+[a-zA-Z]{2,6}$")
            .matcher(email).matches();

    //TODO validate id 5
    private Mono<Void> validateWithCodeAlert(Message message) {
        return Mono.just(message)
                .map(Message::getIdAlert)
                .flatMap(alertGateway::findAlertById)
                .filter(alert -> alert.getIdState()==(0))
                .filter(alert -> isValidMailAndMobile.test(message))
                .filter(alert -> isValidFormatMail.test(message.getMail()))
                .then(Mono.empty());
    }

    //TODO validate id 3 y 4
    private Mono<Void> validateOthersChannels(Message message) {
        return Mono.empty();
    }
    private  Mono<Void> buildMessageFrame(){
        return Mono.empty();
    }


    //TODO validate id 2
    private Mono<Void> validateWithCodeTrx(Message message) {
        return Mono.just(message)
                .filter(isValidMailAndMobile)
                .flatMap(alertTransactionGateway::findAllAlertTransaction)
                .then(Mono.empty());
    }


    //TODO validate id 1
    private Mono<Void> validateBasic(Message message) {
        return Mono.just(message)
                .flatMap(this::validateConsumer)
                .filter(isValidMailAndMobile)
                .then(Mono.empty());
    }


    //TODO validate id 0

    private Mono<Alert> validateAmount(Alert alert, Message message){
        return Mono.just(AlertClient.builder().build())
                .flatMap(alertClientGateway::findAlertClient)
                .filter(alertClient -> alertClient.getAccumulatedAmount()+message.getAmount()<=alertClient.getAmountEnable())
                .map(alertClient -> alert)
                .switchIfEmpty(Mono.error(new Throwable()));
    }
    private Flux<Alert> validateObligation(Alert pAlert, Message message){
        return Flux.just(pAlert)
                .filter(alert -> !alert.getObligatory())
                .filter(alert -> alert.getNature().equals("MO"))
                .flatMap(alert -> validateAmount(alert, message))
                .switchIfEmpty(Mono.just(pAlert))
                .onErrorResume(throwable -> Mono.empty());
    }

    private Mono<Void> validateAlerts(List<AlertTransaction> alertTransactions, Message message){
        return Flux.fromIterable(alertTransactions)
                .map(AlertTransaction::getIdAlert)
                .flatMap(alertGateway::findAlertById)
                .flatMap(alert -> validateObligation(alert, message))
                .then(Mono.empty());
    }

    private Mono<Message> validateConsumer(Message message){
        return Mono.empty();
    }

    private Mono<Message> validateDataContact(Message message){
        return Mono.just(message)
                .filter(isValidMailAndMobile)
                .switchIfEmpty(Mono.just("Obtener mobile contacto")
                        .map(contact -> Message.builder().mobile(contact).mail(contact).build()));

    }

    private Mono<Void> validateWithDataClient(Message message) {
        return Mono.just(message)
                .flatMap(clientGateway::findClientByIdentification)
                .filter(client -> client.getIdState()==0)
                .map(client -> message)
                .flatMap(this::validateDataContact)
                .filter(isValidMailAndMobile)
                .flatMap(this::validateConsumer)
                .flatMap(alertTransactionGateway::findAllAlertTransaction)
                .flatMap(alertTransactions -> validateAlerts(alertTransactions, message))
                .then(Mono.empty());
    }

    public Mono<Void> alertSendingManager(Message message) {
        return Mono.just(message.getIdOperation())
                .map(this::getFunction)
                .flatMap(function -> function.apply(message));
    }

}
