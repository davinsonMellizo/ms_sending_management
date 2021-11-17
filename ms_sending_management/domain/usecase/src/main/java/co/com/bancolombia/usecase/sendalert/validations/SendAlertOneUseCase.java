package co.com.bancolombia.usecase.sendalert.validations;

import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.alert.gateways.AlertGateway;
import co.com.bancolombia.model.message.*;
import co.com.bancolombia.usecase.sendalert.RouterProviderMailUseCase;
import co.com.bancolombia.usecase.sendalert.RouterProviderPushUseCase;
import co.com.bancolombia.usecase.sendalert.RouterProviderSMSUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class SendAlertOneUseCase {
    private final RouterProviderMailUseCase routerProviderMailUseCase;
    private final RouterProviderPushUseCase routerProviderPushUseCase;
    private final RouterProviderSMSUseCase routerProviderSMSUseCase;
    private final AlertGateway alertGateway;

    private Mono<Void> routingAlerts(Message message, Alert pAlert){
        return Mono.just(pAlert)//TODO poner el mensaje en alert
                .filter(alert -> alert.getPush().equalsIgnoreCase("Si"))
                .flatMap(alert -> routerProviderPushUseCase.sendPush(message, alert))
                .switchIfEmpty(routerProviderSMSUseCase.routingAlertsSMS(message, pAlert))
                //TODO mirar si el parametro se envia asì
                .concatWith(routerProviderMailUseCase.sendMail(message, pAlert, message.getParameters()))
                .thenEmpty(Mono.empty());
    }

    public Mono<Void> validateBasic(Message message) {
        return alertGateway.findAlertById("GNR")
                .map(alert -> alert.toBuilder().message(message.getParameters().get(0).getValue()).build())
                .flatMap(alert -> routingAlerts(message, alert));
    }

}
