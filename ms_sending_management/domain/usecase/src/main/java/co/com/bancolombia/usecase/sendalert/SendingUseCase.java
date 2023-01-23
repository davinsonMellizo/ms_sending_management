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
import java.util.UUID;

import static co.com.bancolombia.commons.constants.Constants.MONETARY;
import static co.com.bancolombia.commons.constants.Constants.TRX_580;
import static co.com.bancolombia.commons.constants.State.ACTIVE;
import static co.com.bancolombia.commons.constants.TypeLogSend.SEND_220;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.*;
import static co.com.bancolombia.usecase.sendalert.commons.ValidateData.isValidMailFormatOrMobile;

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

    private Flux<Void> routeAlert(Alert alert, Message message) {
        return Mono.just(message)
                .flatMap(message1 -> routerProviderPushUseCase.sendPush(message, alert))
                .concatWith(routerProviderSMSUseCase.routeAlertsSMS(message, alert))
                .concatWith(routerProviderMailUseCase.routeAlertMail(message, alert))
                .thenMany(Flux.empty());
    }

    private Flux<Void> validateAlerts(Message message) {
        return Mono.just(message)
                .filter(message1 -> !message1.getTransactionCode().isEmpty())
                .filter(message1 -> !message1.getConsumer().isEmpty())
                .flatMapMany(alertUseCase::getAlertsByTransactions)
                .switchIfEmpty(alertUseCase.getAlert(message))
                .filter(alert -> alert.getIdState().equals(ACTIVE))
                .switchIfEmpty(Mono.error(new BusinessException(INACTIVE_ALERT)))
                .flatMap(alert -> validateObligation(alert, message))
                .flatMap(alert -> buildMessageSms(alert, message))
                .flatMap(alert -> routeAlert(alert, message));
    }

    private Mono<Alert> buildMessageSms(Alert alert, Message message){
        return Mono.just(message)
                .filter(message1 -> message1.getParameters().containsKey("mensaje"))
                .map(message1 -> alert.toBuilder().message(message1.getParameters().get("mensaje")).build())
                .switchIfEmpty(Util.replaceParameter(alert, message));

    }

    private Mono<Message> validateData(Message message){
        return Mono.just(message)
                .filter(isValidMailFormatOrMobile)
                .switchIfEmpty(Mono.error(new BusinessException(INVALID_CONTACTS)));
    }

    public Mono<Void> alertSendingManager(Message message) {
        var sizeLogKey = 16;
        String logKey = LocalDate.now().toString()+UUID.randomUUID().toString().substring(sizeLogKey);
        return Mono.just(message)
                .filter(message1 -> message1.getDocumentNumber() != null && message1.getDocumentType() != null )
                .flatMap(clientUseCase::validateClient)
                .switchIfEmpty(validateData(message))
                .flatMapMany(this::validateAlerts)
                .onErrorResume(e -> logUseCase.sendLogError(message.toBuilder().logKey(logKey).build(),
                        SEND_220, Response.builder().code(1).description(e.getMessage()).build()))
                .then(Mono.empty());
    }
}
