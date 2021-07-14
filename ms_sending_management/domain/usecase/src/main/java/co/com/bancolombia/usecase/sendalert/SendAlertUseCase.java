package co.com.bancolombia.usecase.sendalert;

import co.com.bancolombia.model.alert.gateways.AlertGateway;
import co.com.bancolombia.model.alerttransaction.gateways.AlertTransactionGateway;
import co.com.bancolombia.model.message.Message;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class SendAlertUseCase {
    private final AlertTransactionGateway alertTransactionGateway;
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

    public Mono<Void> sendAlert(Message message) {
        return Mono.just(message.getIdOperation())
                .map(this::getFunction)
                .flatMap(function -> function.apply(message));
    }

    public Mono<Void> validateBasic(Message message) {
        return Mono.empty();
    }

    public Mono<Void> validateWithCodeAlert(Message message) {
        return Mono.empty();
    }
    public Mono<Void> validateWithCodeTrx(Message message) {
        return Mono.empty();
    }

    public Mono<Void> validateWithDataClient(Message message) {
        return Mono.empty();
    }

    public Mono<Void> validateOthersChannels(Message message) {
        return Mono.empty();
    }

    public Boolean validaEmail (String email) {
        Pattern pattern = Pattern.compile("^([0-9a-zA-Z]+[-._+&])*[0-9a-zA-Z]+@([-0-9a-zA-Z]+[.])+[a-zA-Z]{2,6}$");
        return pattern.matcher(email).matches();
    }


    public Mono<Void> sendAlertRetry(Message message){
        return Mono.empty();
    }
}
