package co.com.bancolombia.usecase.sendalert.operations;

import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.alert.gateways.AlertGateway;
import co.com.bancolombia.model.alerttransaction.AlertTransaction;
import co.com.bancolombia.model.alerttransaction.gateways.AlertTransactionGateway;
import co.com.bancolombia.model.consumer.gateways.ConsumerGateway;
import co.com.bancolombia.model.message.Message;
import co.com.bancolombia.usecase.sendalert.RouterProviderPushUseCase;
import co.com.bancolombia.usecase.sendalert.RouterProviderSMSUseCase;
import co.com.bancolombia.usecase.sendalert.commons.Util;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.constants.Constants.SI;
import static co.com.bancolombia.commons.constants.Constants.TRX_580;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.ALERT_NOT_FOUND;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.ALERT_TRANSACTION_NOT_FOUND;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.CONSUMER_NOT_FOUND;

@RequiredArgsConstructor
public class SendAlertThreeUseCase {
    private final RouterProviderPushUseCase routerProviderPushUseCase;
    private final RouterProviderSMSUseCase routerProviderSMSUseCase;
    private final AlertTransactionGateway alertTransactionGateway;
    private final ConsumerGateway consumerGateway;
    private final ValidateContactUseCase validateContactUseCase;
    private final AlertGateway alertGateway;
    private final ValidateAmountUseCase validateAmountUseCase;

    private Flux<Alert> validateTransaction(Alert alert, Message pMessage) {
        return Flux.just(pMessage)
                .filter(message ->  message.getTransactionCode().equals(TRX_580))
                .flatMap(message -> validateAmountUseCase.validateAmount(alert, message))
                .switchIfEmpty(Mono.just(alert));
    }

    private Flux<Void> routeAlert(Alert alert, Message message) {
        return Mono.just(message)
                .filter(message1 -> message1.getPush() && alert.getPush().equalsIgnoreCase(SI))
                .flatMap(message1 -> routerProviderPushUseCase.sendPush(message1, alert))
                .switchIfEmpty(routerProviderSMSUseCase.routeAlertsSMS(message, alert))
                .thenMany(Flux.empty());
    }

    private Flux<Void> validateAlerts(Message message) {
        return alertTransactionGateway.findAllAlertTransaction(message)
                .switchIfEmpty(Mono.error(new BusinessException(ALERT_TRANSACTION_NOT_FOUND)))
                .map(AlertTransaction::getIdAlert)
                .flatMap(alertGateway::findAlertById)
                .switchIfEmpty(Mono.error(new BusinessException(ALERT_NOT_FOUND)))
                .flatMap(alert -> validateTransaction(alert, message))
                .flatMap(alert -> Util.replaceParameter(alert, message))
                .flatMap(alert -> routeAlert(alert, message));
    }

    public Mono<Void> validateOthersChannels(Message message) {
        return consumerGateway.findConsumerById(message.getConsumer())
                .switchIfEmpty(Mono.error(new BusinessException(CONSUMER_NOT_FOUND)))
                .flatMap(consumer -> validateContactUseCase.validateDataContact(message, consumer))
                .flatMapMany(this::validateAlerts)
                .then(Mono.empty());
    }
}
