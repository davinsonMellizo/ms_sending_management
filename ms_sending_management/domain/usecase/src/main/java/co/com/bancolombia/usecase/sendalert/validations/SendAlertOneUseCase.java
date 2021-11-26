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
                .map(alert -> alert.toBuilder().message(message.getParameters().get(0).getValue()).build())
                .flatMap(alert -> routeAlerts(message, alert));
    }

}
