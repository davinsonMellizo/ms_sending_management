package co.com.bancolombia.usecase.sendalert.validations;

import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.alert.gateways.AlertGateway;
import co.com.bancolombia.model.alertclient.gateways.AlertClientGateway;
import co.com.bancolombia.model.alerttransaction.AlertTransaction;
import co.com.bancolombia.model.alerttransaction.gateways.AlertTransactionGateway;
import co.com.bancolombia.model.consumer.gateways.ConsumerGateway;
import co.com.bancolombia.model.contact.gateways.ContactGateway;
import co.com.bancolombia.model.message.Message;
import co.com.bancolombia.model.message.Response;
import co.com.bancolombia.usecase.log.LogUseCase;
import co.com.bancolombia.usecase.sendalert.RouterProviderMailUseCase;
import co.com.bancolombia.usecase.sendalert.RouterProviderPushUseCase;
import co.com.bancolombia.usecase.sendalert.RouterProviderSMSUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.constants.TypeLogSend.SEND_220;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.*;

@RequiredArgsConstructor
public class SendAlertThreeUseCase {
    private final RouterProviderPushUseCase routerProviderPushUseCase;
    private final RouterProviderMailUseCase routerProviderMailUseCase;
    private final RouterProviderSMSUseCase routerProviderSMSUseCase;
    private final AlertTransactionGateway alertTransactionGateway;
    private final AlertClientGateway alertClientGateway;
    private final ConsumerGateway consumerGateway;
    private final ContactGateway contactGateway;
    private final ValidateContactUseCase validateContactUseCase;
    private final AlertGateway alertGateway;
    private final LogUseCase logUseCase;
    private final ValidateAmountUseCase validateAmountUseCase;

    private Flux<Alert> validateTransaction(Alert alert, Message pMessage) {
        return Flux.just(pMessage)
                .filter(message ->  message.getTransactionCode().equals("580"))
                .flatMap(message -> validateAmountUseCase.validateAmount(alert, message))
                .switchIfEmpty(Mono.just(alert))
                .onErrorResume(BusinessException.class, e -> logUseCase.sendLogError(pMessage, SEND_220,
                        new Response(1, e.getBusinessErrorMessage().getMessage())));
    }

    private Flux<Void> routeAlert(Alert alert, Message message) {
        return Mono.just(message)
                .filter(message1 -> message1.getPush() && alert.getPush().equalsIgnoreCase("Si"))
                .flatMap(message1 -> routerProviderPushUseCase.sendPush(message1, alert))
                .switchIfEmpty(routerProviderSMSUseCase.routeAlertsSMS(message, alert))
                .thenMany(Flux.empty());
    }

    private Flux<Void> validateAlerts(Message message) {
        return alertTransactionGateway.findAllAlertTransaction(message)
                .map(AlertTransaction::getIdAlert)
                .flatMap(alertGateway::findAlertById)
                .switchIfEmpty(logUseCase.sendLogError(message, SEND_220, new Response(1, ALERT_NOT_FOUND)))
                .onErrorResume(BusinessException.class, e -> logUseCase.sendLogError(message, SEND_220,
                        new Response(1, e.getBusinessErrorMessage().getMessage())))
                .flatMap(alert -> validateTransaction(alert, message))
                .flatMap(alert -> routeAlert(alert, message));
    }

    public Mono<Void> validateOthersChannels(Message message) {
        return consumerGateway.findConsumerById(message.getConsumer())
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
