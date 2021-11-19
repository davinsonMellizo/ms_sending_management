package co.com.bancolombia.usecase.sendalert.validations;

import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.alert.gateways.AlertGateway;
import co.com.bancolombia.model.message.Message;
import co.com.bancolombia.model.message.Response;
import co.com.bancolombia.usecase.log.LogUseCase;
import co.com.bancolombia.usecase.sendalert.RouterProviderMailUseCase;
import co.com.bancolombia.usecase.sendalert.RouterProviderPushUseCase;
import co.com.bancolombia.usecase.sendalert.RouterProviderSMSUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.constants.TypeLogSend.SEND_220;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.INVALID_CONTACT;
import static co.com.bancolombia.usecase.sendalert.commons.ValidateData.isValidMail;

@RequiredArgsConstructor
public class SendAlertOneUseCase {
    private final RouterProviderMailUseCase routerProviderMailUseCase;
    private final RouterProviderPushUseCase routerProviderPushUseCase;
    private final RouterProviderSMSUseCase routerProviderSMSUseCase;
    private final LogUseCase logUseCase;
    private final AlertGateway alertGateway;

    private Mono<Response> validateMail(Message message, Alert alert) {
        return Mono.just(message)
                .filter(isValidMail)
                .switchIfEmpty(Mono.error(new BusinessException(INVALID_CONTACT)))
                .map(Message::getParameters)
                .flatMap(parameters -> routerProviderMailUseCase.routeAlertMail(message, alert, parameters))
                .onErrorResume(BusinessException.class, e -> logUseCase.sendLogMAIL(message, alert, SEND_220,
                        alert.getMessage(), new Response(1, e.getBusinessErrorMessage().getMessage())));
    }

    private Mono<Void> routeAlerts(Message message, Alert pAlert) {
        return Mono.just(pAlert)
                .filter(alert -> alert.getPush().equalsIgnoreCase("Si"))
                .flatMap(alert -> routerProviderPushUseCase.sendPush(message, alert))
                .switchIfEmpty(routerProviderSMSUseCase.validateMobile(message, pAlert))
                .concatWith(validateMail(message, pAlert))
                .thenEmpty(Mono.empty());
    }

    public Mono<Void> sendAlertIndicatorOne(Message message) {
        return alertGateway.findAlertById("GNR")
                .map(alert -> alert.toBuilder().message(message.getParameters().get(0).getValue()).build())
                .flatMap(alert -> routeAlerts(message, alert));
    }

}
