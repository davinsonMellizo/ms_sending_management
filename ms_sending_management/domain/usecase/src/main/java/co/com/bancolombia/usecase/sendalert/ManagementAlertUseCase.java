package co.com.bancolombia.usecase.sendalert;

import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.message.Message;
import co.com.bancolombia.model.message.Response;
import co.com.bancolombia.usecase.log.LogUseCase;
import co.com.bancolombia.usecase.sendalert.operations.SendAlertFiveUseCase;
import co.com.bancolombia.usecase.sendalert.operations.SendAlertOneUseCase;
import co.com.bancolombia.usecase.sendalert.operations.SendAlertThreeUseCase;
import co.com.bancolombia.usecase.sendalert.operations.SendAlertTwoUseCase;
import co.com.bancolombia.usecase.sendalert.operations.SendAlertZeroUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
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
        final Map<Integer, Function<Message, Mono<Void>>> functions = new ConcurrentHashMap<>();
        functions.put(0, sendAlertZeroUseCase::indicatorZero);
        functions.put(1, sendAlertOneUseCase::sendAlertIndicatorOne);
        functions.put(2, sendAlertTwoUseCase::validateWithCodeTrx);
        functions.put(3, sendAlertThreeUseCase::validateOthersChannels);
        functions.put(4, sendAlertThreeUseCase::validateOthersChannels);
        functions.put(5, sendAlertFiveUseCase::sendAlertIndicatorFive);

        return functions.get(idOperation);
    }

    public Mono<Void> alertSendingManager(Message message) {
        var sizeLogKey = 16;
        String logKey = LocalDate.now().toString()+UUID.randomUUID().toString().substring(sizeLogKey);
        return Mono.just(message.getOperation())
                .map(this::getValidations)
                .onErrorMap(e-> new BusinessException(INVALID_OPERATION))
                .flatMap(function -> function.apply(message.toBuilder().logKey(logKey).build()))
                .onErrorResume(e -> logUseCase.sendLogError(message.toBuilder().logKey(logKey).build(),
                        SEND_220, Response.builder().code(1).description(e.getMessage()).build()));
    }

}
