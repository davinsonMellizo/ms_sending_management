package co.com.bancolombia.usecase.sendalert.operations;

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

import static co.com.bancolombia.commons.constants.Constants.SI;
import static co.com.bancolombia.commons.constants.State.ACTIVE;
import static co.com.bancolombia.commons.constants.TypeLogSend.SEND_220;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.ALERT_NOT_FOUND;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.INACTIVE_ALERT;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.INVALID_CONTACT;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.INVALID_CONTACTS;
import static co.com.bancolombia.usecase.sendalert.commons.ValidateData.isValidMailFormat;
import static co.com.bancolombia.usecase.sendalert.commons.ValidateData.isValidMailFormatOrMobile;

@RequiredArgsConstructor
public class SendAlertFiveUseCase {
    private final RouterProviderPushUseCase routerProviderPushUseCase;
    private final RouterProviderMailUseCase routerProviderMailUseCase;
    private final RouterProviderSMSUseCase routerProviderSMSUseCase;
    private final AlertGateway alertGateway;
    private final LogUseCase logUseCase;

    private Mono<Response> validateMail(Message message, Alert alert) {
        return Mono.just(message)
                .filter(isValidMailFormat)
                .switchIfEmpty(Mono.error(new BusinessException(INVALID_CONTACT)))
                .flatMap(messageValid -> routerProviderMailUseCase.routeAlertMail(messageValid, alert))
                .onErrorResume(BusinessException.class, e -> logUseCase.sendLogMAIL(message, alert, SEND_220,
                       new Response(1, e.getBusinessErrorMessage().getMessage())));
    }

    private Mono<Void> routeAlert(Alert alert, Message message) {
        return Mono.just(message)
                .filter(message1 -> alert.getPush().equalsIgnoreCase(SI))
                .flatMap(message1 -> routerProviderPushUseCase.sendPush(message1, alert))
                .switchIfEmpty(routerProviderSMSUseCase.validateMobile(message, alert))
                .concatWith(validateMail(message, alert))
                .then(Mono.empty());
    }

    public Mono<Void> sendAlertIndicatorFive(Message message) {
        return Mono.just(message)
                .filter(isValidMailFormatOrMobile)
                .switchIfEmpty(Mono.error(new BusinessException(INVALID_CONTACTS)))
                .map(Message::getAlert)
                .flatMap(alertGateway::findAlertById)
                .switchIfEmpty(Mono.error(new BusinessException(ALERT_NOT_FOUND)))
                .filter(alert -> alert.getIdState() == ACTIVE)
                .switchIfEmpty(Mono.error(new BusinessException(INACTIVE_ALERT)))
                .flatMap(alert -> Util.replaceParameter(alert, message))
                .flatMap(alert -> routeAlert(alert, message));
    }

}
