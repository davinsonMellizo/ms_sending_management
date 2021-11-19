package co.com.bancolombia.usecase.sendalert.validations;

import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.alert.gateways.AlertGateway;
import co.com.bancolombia.model.alertclient.AlertClient;
import co.com.bancolombia.model.alertclient.gateways.AlertClientGateway;
import co.com.bancolombia.model.alerttransaction.AlertTransaction;
import co.com.bancolombia.model.alerttransaction.gateways.AlertTransactionGateway;
import co.com.bancolombia.model.client.gateways.ClientGateway;
import co.com.bancolombia.model.consumer.Consumer;
import co.com.bancolombia.model.consumer.gateways.ConsumerGateway;
import co.com.bancolombia.model.contact.Contact;
import co.com.bancolombia.model.contact.gateways.ContactGateway;
import co.com.bancolombia.model.message.Message;
import co.com.bancolombia.model.message.Response;
import co.com.bancolombia.usecase.log.LogUseCase;
import co.com.bancolombia.usecase.sendalert.RouterProviderMailUseCase;
import co.com.bancolombia.usecase.sendalert.RouterProviderPushUseCase;
import co.com.bancolombia.usecase.sendalert.RouterProviderSMSUseCase;
import co.com.bancolombia.usecase.sendalert.commons.Util;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

import static co.com.bancolombia.commons.constants.State.ACTIVE;
import static co.com.bancolombia.commons.constants.TypeLogSend.SEND_220;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.*;

@RequiredArgsConstructor
public class SendAlertZeroUseCase {
    private final RouterProviderPushUseCase routerProviderPushUseCase;
    private final RouterProviderMailUseCase routerProviderMailUseCase;
    private final RouterProviderSMSUseCase routerProviderSMSUseCase;
    private final AlertTransactionGateway alertTransactionGateway;
    private final AlertClientGateway alertClientGateway;
    private final ConsumerGateway consumerGateway;
    private final ContactGateway contactGateway;
    private final ClientGateway clientGateway;
    private final AlertGateway alertGateway;
    private final LogUseCase logUseCase;

    private Mono<AlertClient> restartAccumulated(AlertClient pAlertClient) {
        LocalDate now = LocalDate.now();
        return Mono.just(pAlertClient)
                .map(alertClient -> alertClient.getTransactionDate().toLocalDate())
                .filter(dateTransaction -> !dateTransaction.equals(now))
                .map(dateTransaction -> pAlertClient.toBuilder().accumulatedOperations(0)
                        .accumulatedAmount(0L).build())
                .switchIfEmpty(Mono.just(pAlertClient));
    }

    private Mono<Alert> validateAmount(Alert alert, Message message) {
        return Mono.just(AlertClient.builder().idAlert(alert.getId()).documentType(message.getDocumentType())
                .documentNumber(message.getDocumentNumber()).build())
                .flatMap(alertClientGateway::findAlertClient)
                .flatMap(this::restartAccumulated)
                .map(alertClient -> alertClient.toBuilder()
                        .accumulatedOperations(alertClient.getAccumulatedOperations() + 1)
                        .accumulatedAmount(alertClient.getAccumulatedAmount() + message.getAmount()).build())
                .switchIfEmpty(Mono.error(new BusinessException(ALERT_CLIENT_NOT_FOUND)))
                .flatMap(alertClientGateway::accumulate)
                .filter(alertClient -> alertClient.getAccumulatedAmount() >= alertClient.getAmountEnable() ||
                        alertClient.getAccumulatedOperations() >= alertClient.getNumberOperations())
                .map(alertClient -> alert)
                .switchIfEmpty(Mono.error(new BusinessException(AMOUNT_NOT_EXCEEDED)));
    }

    private Flux<Alert> validateObligation(Alert pAlert, Message message) {
        return Flux.just(pAlert)
                .filter(alert -> !alert.getObligatory())
                .filter(alert -> alert.getNature().equals("MO"))
                .flatMap(alert -> validateAmount(alert, message))
                .switchIfEmpty(Mono.just(pAlert))
                .onErrorResume(BusinessException.class, e -> logUseCase.sendLogError(message, SEND_220,
                        new Response(1, e.getBusinessErrorMessage().getMessage())));
    }

    private Mono<Message> validateDataContact(Message message, Consumer consumer) {
        return contactGateway.findAllContactsByClient(message.toBuilder().consumer(consumer.getSegment()).build())
                .collectMap(Contact::getContactMedium)
                .map(contacts -> message.toBuilder()
                        .phone(contacts.get("SMS") != null ? contacts.get("SMS").getValue() : "")
                        .push(contacts.get("PUSH") != null ? contacts.get("PUSH").getIdState() == ACTIVE ? true : false : false)
                        .mail(contacts.get("MAIL") != null ? contacts.get("MAIL").getValue() : "").build())
                .switchIfEmpty(Mono.error(new BusinessException(INVALID_CONTACTS)))
                .onErrorResume(BusinessException.class, e -> logUseCase.sendLogError(message, SEND_220,
                        new Response(1, e.getBusinessErrorMessage().getMessage())));
    }

    private Flux<Void> routeAlert(Alert alert, Message message) {
        return Mono.just(message)
                .filter(message1 -> message1.getPush() && alert.getPush().equalsIgnoreCase("Si"))
                .flatMap(message1 -> routerProviderPushUseCase.sendPush(message1, alert))
                .switchIfEmpty(routerProviderSMSUseCase.routeAlertsSMS(message, alert))
                .concatWith(routerProviderMailUseCase.sendAlertMail(alert, message))
                .thenMany(Flux.empty());
    }

    private Flux<Alert> validateAlerts(Message message) {
        return alertTransactionGateway.findAllAlertTransaction(message)
                .map(AlertTransaction::getIdAlert)
                .flatMap(alertGateway::findAlertById)
                .switchIfEmpty(logUseCase.sendLogError(message, SEND_220, new Response(1, ALERT_NOT_FOUND)))
                .flatMap(alert -> Util.replaceParameter(alert, message))
                .onErrorResume(BusinessException.class, e -> logUseCase.sendLogError(message, SEND_220,
                        new Response(1, e.getBusinessErrorMessage().getMessage())))
                .flatMap(alert -> validateObligation(alert, message));
    }

    public Mono<Void> sendAlertsIndicatorZero(Message message) {
        return Mono.just(message)
                .flatMap(message1 -> clientGateway.findClientByIdentification(message.getDocumentNumber(), message.getDocumentType()))
                .switchIfEmpty(Mono.error(new BusinessException(CLIENT_NOT_FOUND)))
                .filter(client -> client.getIdState() == 0)
                .switchIfEmpty(Mono.error(new BusinessException(CLIENT_INACTIVE)))
                .flatMap(client -> consumerGateway.findConsumerById(message.getConsumer()))
                .switchIfEmpty(Mono.error(new BusinessException(CONSUMER_NOT_FOUND)))
                .onErrorResume(BusinessException.class, e -> logUseCase.sendLogError(message, SEND_220,
                        new Response(1, e.getBusinessErrorMessage().getMessage())))
                .flatMap(consumer -> validateDataContact(message, consumer))
                .flatMapMany(this::validateAlerts)
                .flatMap(alert -> routeAlert(alert, message))
                .onErrorResume(TechnicalException.class, e -> logUseCase.sendLogError(message, SEND_220,
                        new Response(1, e.getMessage())))
                .then(Mono.empty());
    }
}