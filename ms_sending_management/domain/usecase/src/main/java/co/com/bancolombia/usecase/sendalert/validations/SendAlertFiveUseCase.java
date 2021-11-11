package co.com.bancolombia.usecase.sendalert.validations;

import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.alert.gateways.AlertGateway;
import co.com.bancolombia.model.message.Message;
import co.com.bancolombia.model.message.Response;
import co.com.bancolombia.usecase.log.LogUseCase;
import co.com.bancolombia.usecase.sendalert.RouterProviderMailUseCase;
import co.com.bancolombia.usecase.sendalert.RouterProviderPushUseCase;
import co.com.bancolombia.usecase.sendalert.RouterProviderSMSUseCase;
import co.com.bancolombia.usecase.sendalert.commons.Util;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.constants.TypeLogSend.SEND_220;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.*;
import static co.com.bancolombia.usecase.sendalert.commons.ValidateData.*;

@RequiredArgsConstructor
public class SendAlertFiveUseCase {
    private final RouterProviderPushUseCase routerProviderPushUseCase;
    private final RouterProviderMailUseCase routerProviderMailUseCase;
    private final RouterProviderSMSUseCase routerProviderSMSUseCase;
    private final AlertGateway alertGateway;
    private final LogUseCase logUseCase;

    private Mono<Void> sendAlert(Alert alert, Message message) {
        return Mono.just(message)
                .filter(message1 -> !message1.getPush() || alert.getPush().equalsIgnoreCase("No"))
                .flatMap(message1 -> routerProviderSMSUseCase.routingAlertsSMS(message1, alert))
                .switchIfEmpty(routerProviderPushUseCase.sendPush(message,alert))
                .concatWith(message1 -> routerProviderMailUseCase.sendAlertMail(alert, message))
                .then(Mono.empty());
    }

    public Mono<Void> validateWithCodeAlert(Message message) {
        return Mono.just(message)
                .filter(isValidMailFormatOrMobile)
                .switchIfEmpty(Mono.error(new BusinessException(INVALID_CONTACTS)))
                .map(Message::getAlert)
                .flatMap(alertGateway::findAlertById)
                .switchIfEmpty(Mono.error(new BusinessException(ALERT_NOT_FOUND)))
                .filter(alert -> alert.getIdState()==(0))
                .switchIfEmpty(Mono.error(new BusinessException(INACTIVE_ALERT)))
                .flatMapMany(alert -> Util.replaceParameter(alert, message)).next()
                .flatMap(alert -> sendAlert(alert, message))
                .onErrorResume(BusinessException.class, e -> logUseCase.sendLogError(message,SEND_220,
                        new Response(1, e.getBusinessErrorMessage().getMessage())))
                .onErrorResume(TechnicalException.class, e -> logUseCase.sendLogError(message,SEND_220,
                        new Response(1, e.getMessage())));
    }

}
