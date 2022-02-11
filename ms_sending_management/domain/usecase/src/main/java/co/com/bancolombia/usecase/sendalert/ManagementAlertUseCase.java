package co.com.bancolombia.usecase.sendalert;

import co.com.bancolombia.model.message.Message;
import co.com.bancolombia.model.message.Response;
import co.com.bancolombia.usecase.log.LogUseCase;
import co.com.bancolombia.usecase.sendalert.operations.*;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import static co.com.bancolombia.commons.constants.TypeLogSend.SEND_220;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.INVALID_OPERATION;

@RequiredArgsConstructor
public class ManagementAlertUseCase {
    private final LogUseCase logUseCase;

    private final SendAlertZeroUseCase sendAlertZeroUseCase;
    private final SendAlertOneUseCase sendAlertOneUseCase;
    private final SendAlertTwoUseCase sendAlertTwoUseCase;
    private final SendAlertThreeUseCase sendAlertThreeUseCase;
    private final SendAlertFiveUseCase sendAlertFiveUseCase;

    private Function<Message, Mono<Void>> getValidations(Integer idOperation) {
        final Map<Integer, Function<Message, Mono<Void>>> functions = new HashMap<>();
        functions.put(0, sendAlertZeroUseCase::sendAlertsIndicatorZero);
        functions.put(1, sendAlertOneUseCase::sendAlertIndicatorOne);
        functions.put(2, sendAlertTwoUseCase::validateWithCodeTrx);
        functions.put(3, sendAlertThreeUseCase::validateOthersChannels);
        functions.put(4, sendAlertThreeUseCase::validateOthersChannels);
        functions.put(5, sendAlertFiveUseCase::sendAlertIndicatorFive);

        return functions.get(idOperation);
    }

    public Mono<Void> alertSendingManager(Message message) {
        return Mono.just(message.getOperation())
                .map(this::getValidations)
                .filter(function -> function != null)
                .switchIfEmpty(logUseCase.sendLogError(message.toBuilder().logKey(UUID.randomUUID().toString()).build(),
                        SEND_220, new Response(1, INVALID_OPERATION)))
                .flatMap(function -> function.apply(message.toBuilder().logKey(UUID.randomUUID().toString()).build()));
    }
}
