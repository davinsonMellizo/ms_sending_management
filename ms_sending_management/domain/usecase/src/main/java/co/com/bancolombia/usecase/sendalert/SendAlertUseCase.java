package co.com.bancolombia.usecase.sendalert;

import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.alert.gateways.AlertGateway;
import co.com.bancolombia.model.alertclient.AlertClient;
import co.com.bancolombia.model.alertclient.gateways.AlertClientGateway;
import co.com.bancolombia.model.alerttransaction.AlertTransaction;
import co.com.bancolombia.model.alerttransaction.gateways.AlertTransactionGateway;
import co.com.bancolombia.model.client.gateways.ClientGateway;
import co.com.bancolombia.model.consumer.Consumer;
import co.com.bancolombia.model.consumer.gateways.ConsumerGateway;
import co.com.bancolombia.model.contact.Contact;
import co.com.bancolombia.model.contact.gateways.ContactGateway;
import co.com.bancolombia.model.message.Message;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static co.com.bancolombia.usecase.sendalert.commons.ValidateData.*;

@RequiredArgsConstructor
public class SendAlertUseCase {
    private final AlertTransactionGateway alertTransactionGateway;
    private final AlertClientGateway alertClientGateway;
    private final ConsumerGateway consumerGateway;
    private final ContactGateway contactGateway;
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


    //TODO validate id 5
    private Mono<Void> validateWithCodeAlert(Message message) {
        return Mono.just(message)
                .map(Message::getIdAlert)
                .flatMap(alertGateway::findAlertById)
                .switchIfEmpty(Mono.error(new Throwable("Invalid Code alert")))
                .filter(alert -> alert.getIdState()==(0))
                .switchIfEmpty(Mono.error(new Throwable("Alert not active")))
                .filter(alert -> isValidMobile.test(message) || (isValidMailFormat.test(message)))
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
                .switchIfEmpty(Mono.error(new Throwable("Invalid Data contact")))
                .map(alertTransactionGateway::findAllAlertTransaction)
                .then(Mono.empty());
    }


    //TODO validate id 1
    private Mono<Void> validateBasic(Message message) {
        return Mono.just(message)
                .filter(isValidMailAndMobile)
                .switchIfEmpty(Mono.error(new Throwable("Invalid Data contact")))
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

    private Mono<Void> validateAlerts(Message message){
        return alertTransactionGateway.findAllAlertTransaction(message)
                .map(AlertTransaction::getIdAlert)
                .flatMap(alertGateway::findAlertById)
                .flatMap(alert -> validateObligation(alert, message))
                .then(Mono.empty());
    }

    private Mono<Message> findDataContact(Message message){
        return contactGateway.findAllContactsByClient(message)
                .collectMap(Contact::getContactMedium)
                .map(contacts -> Message.builder()
                        .mobile(contacts.get("SMS").getValue())
                        .mail(contacts.get("MAIL").getValue()).build())
                .switchIfEmpty(Mono.just(message));
    }

    private Mono<Message> validateDataContact(Message message, Consumer consumer){
        return Mono.just(message)
                .filter(isValidMailAndMobile)
                .switchIfEmpty(findDataContact(message))
                .filter(isValidMailAndMobile)
                .switchIfEmpty(findDataContact(Message.builder().consumer(consumer.getCode()).build()))
                .filter(isValidMailAndMobile)
                .switchIfEmpty(Mono.error(new Throwable("Invalid data contact")));

    }

    private Mono<Void> validateWithDataClient(Message message) {
        return Mono.just(message)
                .flatMap(clientGateway::findClientByIdentification)
                .switchIfEmpty(Mono.error(new Throwable("Client not found")))
                .filter(client -> client.getIdState()==0)
                .switchIfEmpty(Mono.error(new Throwable("Client not active")))
                .flatMap(client -> consumerGateway.findConsumerById(message.getConsumer()))
                .switchIfEmpty(Mono.error(new Throwable("Invalid Consumer")))
                .flatMap(consumer -> validateDataContact(message, consumer))
                .flatMap(this::validateAlerts)
                .then(Mono.empty());
    }

    public Mono<Void> alertSendingManager(Message message) {
        return Mono.just(message.getIdOperation())
                .map(this::getFunction)
                .flatMap(function -> function.apply(message));
    }

}
