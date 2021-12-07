package co.com.bancolombia.usecase.sendalert.validations;

import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.alert.gateways.AlertGateway;
import co.com.bancolombia.model.alerttransaction.AlertTransaction;
import co.com.bancolombia.model.alerttransaction.gateways.AlertTransactionGateway;
import co.com.bancolombia.model.client.gateways.ClientGateway;
import co.com.bancolombia.model.consumer.gateways.ConsumerGateway;
import co.com.bancolombia.model.message.Message;
import co.com.bancolombia.model.message.Response;
import co.com.bancolombia.usecase.log.LogUseCase;
import co.com.bancolombia.usecase.sendalert.RouterProviderMailUseCase;
import co.com.bancolombia.usecase.sendalert.RouterProviderPushUseCase;
import co.com.bancolombia.usecase.sendalert.RouterProviderSMSUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.constants.State.ACTIVE;
import static co.com.bancolombia.commons.constants.TypeLogSend.SEND_220;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.*;

@RequiredArgsConstructor
public class SendAlertZeroUseCase {
    private final RouterProviderPushUseCase routerProviderPushUseCase;
    private final RouterProviderMailUseCase routerProviderMailUseCase;
    private final RouterProviderSMSUseCase routerProviderSMSUseCase;
    private final AlertTransactionGateway alertTransactionGateway;
    private final ValidateContactUseCase validateContactUseCase;
    private final ValidateAmountUseCase validateAmountUseCase;
    private final ConsumerGateway consumerGateway;
    private final ClientGateway clientGateway;
    private final AlertGateway alertGateway;
    private final LogUseCase logUseCase;

    private Flux<Alert> validateObligation(Alert pAlert, Message message) {
        return Flux.just(pAlert)
                .filter(alert -> !alert.getObligatory())
                .filter(alert -> alert.getNature().equals("MO"))
                .flatMap(alert -> validateAmountUseCase.validateAmount(alert, message))
                .switchIfEmpty(Mono.just(pAlert))
                .onErrorResume(BusinessException.class, e -> logUseCase.sendLogError(message, SEND_220,
                        new Response(1, e.getBusinessErrorMessage().getMessage())));
    }

    private Flux<Void> routeAlert(Alert alert, Message message) {
        return Mono.just(message)
                .filter(message1 -> message1.getPush() && alert.getPush().equalsIgnoreCase("Si"))
                .flatMap(message1 -> routerProviderPushUseCase.sendPush(message1, alert))
                .switchIfEmpty(routerProviderSMSUseCase.routeAlertsSMS(message, alert))
                .concatWith(routerProviderMailUseCase.routeAlertMail(message, alert))
                .thenMany(Flux.empty());
    }

    private Flux<Void> validateAlerts(Message message) {
        return alertTransactionGateway.findAllAlertTransaction(message)
                .map(AlertTransaction::getIdAlert)
                .flatMap(alertGateway::findAlertById)
                .switchIfEmpty(logUseCase.sendLogError(message, SEND_220, new Response(1, ALERT_NOT_FOUND)))
                .onErrorResume(BusinessException.class, e -> logUseCase.sendLogError(message, SEND_220,
                        new Response(1, e.getBusinessErrorMessage().getMessage())))
                .flatMap(alert -> validateObligation(alert, message))
                .flatMap(alert -> routeAlert(alert, message));
    }

    public Mono<Void> sendAlertsIndicatorZero(Message message) {
        return Mono.just(message)
                .flatMap(message1 -> clientGateway.findClientByIdentification(message.getDocumentNumber(), message.getDocumentType()))
                .switchIfEmpty(Mono.error(new BusinessException(CLIENT_NOT_FOUND)))
                .filter(client -> client.getIdState() == ACTIVE)
                .switchIfEmpty(Mono.error(new BusinessException(CLIENT_INACTIVE)))
                .flatMap(client -> consumerGateway.findConsumerById(message.getConsumer()))
                .switchIfEmpty(Mono.error(new BusinessException(CONSUMER_NOT_FOUND)))
                .onErrorResume(BusinessException.class, e -> logUseCase.sendLogError(message, SEND_220,
                        new Response(1, e.getBusinessErrorMessage().getMessage())))
                .flatMap(consumer -> validateContactUseCase.validateDataContact(message, consumer))
                .flatMapMany(this::validateAlerts)
                .onErrorResume(TechnicalException.class, e -> logUseCase.sendLogError(message, SEND_220,
                        new Response(1, e.getMessage())))
                .then(Mono.empty());
    }
}