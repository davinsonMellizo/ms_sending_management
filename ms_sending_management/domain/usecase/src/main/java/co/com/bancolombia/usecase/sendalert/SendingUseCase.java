package co.com.bancolombia.usecase.sendalert;

import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.message.Message;
import co.com.bancolombia.model.message.Response;
import co.com.bancolombia.usecase.log.LogUseCase;
import co.com.bancolombia.usecase.sendalert.commons.Util;
import co.com.bancolombia.usecase.sendalert.routers.RouterProviderMailUseCase;
import co.com.bancolombia.usecase.sendalert.routers.RouterProviderPushUseCase;
import co.com.bancolombia.usecase.sendalert.routers.RouterProviderSMSUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static co.com.bancolombia.commons.constants.Constants.MONETARY;
import static co.com.bancolombia.commons.constants.Constants.TRX_580;
import static co.com.bancolombia.commons.constants.State.ACTIVE;
import static co.com.bancolombia.commons.constants.TypeLogSend.SEND_220;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.*;

@RequiredArgsConstructor
public class SendingUseCase {
    private final LogUseCase logUseCase;
    private final AlertUseCase alertUseCase;
    private final ClientUseCase clientUseCase;
    private final RouterProviderSMSUseCase routerProviderSMSUseCase;
    private final RouterProviderPushUseCase routerProviderPushUseCase;
    private final RouterProviderMailUseCase routerProviderMailUseCase;

    private Flux<Alert> validateObligation(Alert pAlert, Message message) {
        return Flux.just(pAlert)
                .filter(alert -> (!alert.getObligatory() && !alert.getBasicKit()) || message.getTransactionCode().equals(TRX_580))
                .filter(alert -> alert.getNature().equals(MONETARY))
                .flatMap(alert -> alertUseCase.validateAmount(alert, message))
                .switchIfEmpty(Mono.just(pAlert));
    }

    private Mono<Alert> routeAlert(Alert alert, Message message) {
        return Mono.zip(routerProviderPushUseCase.sendPush(message, alert),
                (routerProviderSMSUseCase.routeAlertsSMS(message, alert)),
                (routerProviderMailUseCase.routeAlertMail(message, alert)))
                .thenReturn(alert);
    }

    private Mono<List<Alert>> validateAlerts(Message message) {
        return Mono.just(message)
                .filter(message1 -> !message1.getTransactionCode().isEmpty())
                .filter(message1 -> !message1.getConsumer().isEmpty())
                .flatMapMany(alertUseCase::getAlertsByTransactions)
                .switchIfEmpty(alertUseCase.getAlert(message))
                .filter(alert -> alert.getIdState().equals(ACTIVE))
                .switchIfEmpty(Mono.error(new BusinessException(INACTIVE_ALERT)))
                .flatMap(alert -> validateObligation(alert, message))
                .flatMap(alert -> buildMessageSms(alert, message))
                .collectList();
    }

    private Mono<Alert> buildMessageSms(Alert alert, Message message){
        return Mono.just(message)
                .filter(message1 -> message1.getParameters().containsKey("mensaje"))
                .map(message1 -> alert.toBuilder().message(message1.getParameters().get("mensaje")).build())
                .switchIfEmpty(Util.replaceParameter(alert, message));
    }

    private Mono<Void> sendAlerts(Message message, List<Alert> alerts){
        return Flux.fromIterable(alerts)
                .flatMap(alert -> routeAlert(alert, message))
                .then(Mono.empty());
    }

    public Mono<Void> alertSendingManager(Message message) {
        var sizeLogKey = 16;
        String logKey = LocalDate.now()+UUID.randomUUID().toString().substring(sizeLogKey);
        message.setLogKey(logKey);
        return Mono.zip(clientUseCase.validateDataContact(message), validateAlerts(message))
                .flatMap(data -> sendAlerts(data.getT1(), data.getT2()))
                .onErrorResume(e -> logUseCase.sendLogError(message.toBuilder().logKey(logKey).build(),
                        SEND_220, Response.builder().code(1).description(e.getMessage()).build()))
                .then(Mono.empty());
    }
}
