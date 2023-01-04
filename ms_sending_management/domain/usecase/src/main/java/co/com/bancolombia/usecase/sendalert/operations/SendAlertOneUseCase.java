package co.com.bancolombia.usecase.sendalert.operations;

import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.alert.gateways.AlertGateway;
import co.com.bancolombia.model.message.Message;
import co.com.bancolombia.usecase.sendalert.RouterProviderMailUseCase;
import co.com.bancolombia.usecase.sendalert.RouterProviderPushUseCase;
import co.com.bancolombia.usecase.sendalert.RouterProviderSMSUseCase;
import co.com.bancolombia.usecase.sendalert.commons.Util;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.enums.BusinessErrorMessage.ALERT_CLIENT_NOT_FOUND;

@RequiredArgsConstructor
public class SendAlertOneUseCase {
    private final RouterProviderMailUseCase routerProviderMailUseCase;
    private final RouterProviderPushUseCase routerProviderPushUseCase;
    private final RouterProviderSMSUseCase routerProviderSMSUseCase;
    private final AlertGateway alertGateway;

    private Mono<Void> routeAlerts(Message message, Alert pAlert) {
        return Mono.just(pAlert)
                .filter(alert -> alert.getPush().equalsIgnoreCase("Si"))
                .flatMap(alert -> routerProviderPushUseCase.sendPush(message, alert))
                .switchIfEmpty(routerProviderSMSUseCase.validateMobile(message, pAlert))
                .concatWith(routerProviderMailUseCase.routeAlertMail(message, pAlert))
                .thenEmpty(Mono.empty());
    }

    public Mono<Void> sendAlertIndicatorOne(Message message) {
        return alertGateway.findAlertById("GNR")
                .switchIfEmpty(Mono.error(new BusinessException(ALERT_CLIENT_NOT_FOUND)))
                .flatMap(alert -> Util.replaceParameter(alert, message))
                .flatMap(alert -> routeAlerts(message, alert));
    }


}
