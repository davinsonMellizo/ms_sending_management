package co.com.bancolombia.usecase.sendalert;

import co.com.bancolombia.model.alert.gateways.AlertGateway;
import co.com.bancolombia.model.alerttransaction.gateways.AlertTransactionGateway;
import co.com.bancolombia.model.message.Message;
import co.com.bancolombia.usecase.sendalert.validations.*;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@RequiredArgsConstructor
public class ManagementAlertUseCase {
    private final AlertTransactionGateway alertTransactionGateway;
    private final AlertGateway alertGateway;

    private final SendAlertZeroUseCase sendAlertZeroUseCase;
    private final SendAlertOneUseCase sendAlertOneUseCase;
    private final SendAlertTwoUseCase sendAlertTwoUseCase;
    private final SendAlertThreeUseCase sendAlertThreeUseCase;
    private final SendAlertFiveUseCase sendAlertFiveUseCase;

    private Function<Message, Mono<Void>> getValidations(Integer idOperation){
        final Map<Integer, Function<Message, Mono<Void>>> functions = new HashMap<>();
        functions.put(0, sendAlertZeroUseCase::validateWithDataClient);
        functions.put(1, sendAlertOneUseCase::validateBasic);
        functions.put(2, sendAlertTwoUseCase::validateWithCodeTrx);
        functions.put(3, sendAlertThreeUseCase::validateOthersChannels);
        functions.put(4, sendAlertThreeUseCase::validateOthersChannels);
        functions.put(5, sendAlertFiveUseCase::validateWithCodeAlert);

        return functions.get(idOperation);
    }

    public Mono<Void> alertSendingManager(Message message) {
        return Mono.just(message.getIdOperation())
                .map(this::getValidations)
                .flatMap(function -> function.apply(message));
    }
}
