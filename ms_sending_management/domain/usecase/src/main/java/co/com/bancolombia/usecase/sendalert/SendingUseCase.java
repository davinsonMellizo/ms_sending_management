package co.com.bancolombia.usecase.sendalert;

import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.message.Message;
import co.com.bancolombia.model.message.Response;
import co.com.bancolombia.model.message.SendResponse;
import co.com.bancolombia.usecase.log.LogUseCase;
import co.com.bancolombia.usecase.sendalert.routers.RouterProviderMailUseCase;
import co.com.bancolombia.usecase.sendalert.routers.RouterProviderPushUseCase;
import co.com.bancolombia.usecase.sendalert.routers.RouterProviderSMSUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuple3;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static co.com.bancolombia.commons.constants.Constants.MONETARY;
import static co.com.bancolombia.commons.constants.Constants.TRX_580;
import static co.com.bancolombia.commons.constants.State.ACTIVE;
import static co.com.bancolombia.commons.constants.TypeLogSend.SEND_220;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.INACTIVE_ALERT;

@RequiredArgsConstructor
public class SendingUseCase {
    private final LogUseCase logUseCase;
    private final AlertUseCase alertUseCase;
    private final ClientUseCase clientUseCase;
    private final RouterProviderSMSUseCase routerProviderSMSUseCase;
    private final RouterProviderPushUseCase routerProviderPushUseCase;
    private final RouterProviderMailUseCase routerProviderMailUseCase;

    private Mono<Alert> validateObligation(Alert pAlert, Message message) {
        return Mono.just(pAlert)
                .filter(alert -> (!alert.getObligatory() && !alert.getBasicKit()) ||
                        message.getTransactionCode().equals(TRX_580))
                .filter(alert -> alert.getNature().equals(MONETARY))
                .flatMap(alert -> alertUseCase.validateAmount(alert, message))
                .switchIfEmpty(Mono.just(pAlert));
    }

    private Flux<Response> routeAlert(Alert pAlert, Message message) {
        return Mono.zip(routerProviderPushUseCase.routeAlertPush(message, pAlert),
                routerProviderSMSUseCase.routeAlertsSMS(message, pAlert),
                routerProviderMailUseCase.routeAlertMail(message, pAlert))
                .map(Tuple3::toList).flatMapMany(Flux::fromIterable).cast(Response.class);
    }
    private Flux<Response> validateAlert(Alert alert, Message message) {
        return Mono.just(alert)
                .filter(alert1 -> alert.getIdState().equals(ACTIVE))
                .switchIfEmpty(Mono.error(new BusinessException(INACTIVE_ALERT)))
                .flatMap(alert1 -> validateObligation(alert, message))
                .flatMapMany(alert1 -> routeAlert(alert, message))
                .onErrorResume(e -> logUseCase.sendLogError(message.toBuilder().logKey(message.getLogKey()).build(),
                        SEND_220, Response.builder().code(1).alert(alert.getId()).description(e.getMessage()).build()));
    }

    private Mono<List<Alert>> findAlert(Message message) {
        return Mono.just(message)
                .filter(message1 -> !message1.getTransactionCode().isEmpty())
                .filter(message1 -> !message1.getConsumer().isEmpty())
                .flatMapMany(alertUseCase::getAlertsByTransactions)
                .switchIfEmpty(alertUseCase.getAlert(message))
                .collectList();
    }

    private Mono<SendResponse.Recipient> validateData(Message message) {
        return Mono.zip(clientUseCase.validateClientInformation(message), findAlert(message))
                .map(Tuple2::getT2)
                .flatMapMany(Flux::fromIterable)
                .flatMap(alert -> validateAlert(alert, message))
                .onErrorResume(e -> logUseCase.sendLogError(message, SEND_220,
                        Response.builder().code(1).description(e.getMessage()).build()))
                .collectList()
                .map(responses -> SendResponse.Recipient.builder().responses(responses)
                        .identification(SendResponse.Identification.builder()
                                .documentNumber(message.getDocumentNumber()).documentType(message.getDocumentType())
                                .build()).build());
    }

    public Mono<SendResponse> sendAlertManager(List<Message> messages) {
        var sizeLogKey = 16;
        String logKey = LocalDate.now() + UUID.randomUUID().toString().substring(sizeLogKey);
        return Flux.fromIterable(messages)
                .doOnNext(message -> message.setLogKey(logKey))
                .flatMap(this::validateData).collectList()
                .map(recipients -> SendResponse.builder().recipients(recipients).logKey(logKey).build());
    }
}
