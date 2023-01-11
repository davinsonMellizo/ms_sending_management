package co.com.bancolombia.usecase.sendalert.operations;

import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.alert.gateways.AlertGateway;
import co.com.bancolombia.model.alerttransaction.AlertTransaction;
import co.com.bancolombia.model.alerttransaction.gateways.AlertTransactionGateway;
import co.com.bancolombia.model.client.gateways.ClientGateway;
import co.com.bancolombia.model.consumer.gateways.ConsumerGateway;
import co.com.bancolombia.model.message.Message;
import co.com.bancolombia.model.message.Response;
import co.com.bancolombia.usecase.sendalert.RouterProviderMailUseCase;
import co.com.bancolombia.usecase.sendalert.RouterProviderPushUseCase;
import co.com.bancolombia.usecase.sendalert.RouterProviderSMSUseCase;
import co.com.bancolombia.usecase.sendalert.commons.Util;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;

import static co.com.bancolombia.commons.constants.Medium.MAIL;
import static co.com.bancolombia.commons.constants.Medium.SMS;
import static co.com.bancolombia.commons.constants.State.ACTIVE;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.ALERT_NOT_FOUND;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.CLIENT_INACTIVE;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.CLIENT_NOT_FOUND;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.CONSUMER_NOT_FOUND;

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

    private Flux<Alert> validateObligation(Alert pAlert, Message message) {
        return Flux.just(pAlert)
                .filter(alert -> !alert.getObligatory())
                .filter(alert -> !alert.getBasicKit())
                .filter(alert -> alert.getNature().equals("MO"))
                .flatMap(alert -> validateAmountUseCase.validateAmount(alert, message))
                .switchIfEmpty(Mono.just(pAlert));
    }

    private Flux<Void> routeAlert(Alert alert, Message message) {
        return Mono.just(message)
                .filter(message1 -> message1.getPush() && alert.getPush().equalsIgnoreCase("Si"))
                .flatMap(message1 -> sendAlert(message1, alert, SMS, routerProviderPushUseCase::sendPush))
                .switchIfEmpty(sendAlert(message, alert, SMS, routerProviderSMSUseCase::routeAlertsSMS))
                .concatWith(sendAlert(message, alert, MAIL, routerProviderMailUseCase::routeAlertMail))
                .thenMany(Flux.empty());
    }
    private Mono<Response> sendAlert(Message message, Alert alert, String medium,
                                     BiFunction<Message, Alert, Mono<Response>> function ){
        return Mono.just(function)
                .filter(bFunction -> message.getPreferences().isEmpty() || message.getPreferences().contains(medium))
                .flatMap(bFunction -> bFunction.apply(message, alert));
    }

    private Flux<Void> validateAlerts(Message message) {
        return alertTransactionGateway.findAllAlertTransaction(message)
                .map(AlertTransaction::getIdAlert)
                .flatMap(alertGateway::findAlertById)
                .switchIfEmpty(Mono.error(new BusinessException(ALERT_NOT_FOUND)))
                .flatMap(alert -> validateObligation(alert, message))
                .flatMap(alert -> Util.replaceParameter(alert, message))
                .flatMap(alert -> routeAlert(alert, message));
    }

    public Mono<Void> indicatorZero(Message message) {
        return Mono.just(message)
                .flatMap(message1 -> clientGateway.findClientByIdentification(message.getDocumentNumber(),
                        message.getDocumentType()))
                .switchIfEmpty(Mono.error(new BusinessException(CLIENT_NOT_FOUND)))
                .filter(client -> client.getIdState() == ACTIVE)
                .switchIfEmpty(Mono.error(new BusinessException(CLIENT_INACTIVE)))
                .flatMap(client -> consumerGateway.findConsumerById(message.getConsumer()))
                .switchIfEmpty(Mono.error(new BusinessException(CONSUMER_NOT_FOUND)))
                .flatMap(consumer -> validateContactUseCase.validateDataContact(message, consumer))
                .flatMapMany(this::validateAlerts)
                .then(Mono.empty());
    }
}