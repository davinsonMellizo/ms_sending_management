package co.com.bancolombia.usecase.sendalert;

import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.alert.gateways.AlertGateway;
import co.com.bancolombia.model.alertclient.AlertClient;
import co.com.bancolombia.model.alertclient.gateways.AlertClientGateway;
import co.com.bancolombia.model.message.Message;
import co.com.bancolombia.usecase.sendalert.commons.UtilParameters;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Objects;

import static co.com.bancolombia.commons.enums.BusinessErrorMessage.*;

@RequiredArgsConstructor
public class AlertUseCase {
    private final AlertGateway alertGateway;
    private final AlertClientGateway alertClientGateway;

    private Mono<AlertClient> restartAccumulated(AlertClient pAlertClient) {
        var now = LocalDate.now();
        return Mono.just(pAlertClient)
                .map(alertClient -> alertClient.getTransactionDate().toLocalDate())
                .filter(dateTransaction -> !dateTransaction.equals(now))
                .map(dateTransaction -> pAlertClient.toBuilder().accumulatedOperations(0)
                        .accumulatedAmount(0L).build())
                .switchIfEmpty(Mono.just(pAlertClient));
    }

    public Mono<Alert> validateAmount(Alert alert, Message pMessage) {
        return Mono.just(pMessage)
                .filter(message1 -> Objects.nonNull(pMessage.getDocumentNumber()))
                .filter(message1 -> Objects.nonNull(pMessage.getDocumentType()))
                .switchIfEmpty(Mono.error(new BusinessException(CLIENT_IDENTIFICATION_INVALID)))
                .map(message -> AlertClient.builder().idAlert(alert.getId()).documentType(pMessage.getDocumentType())
                .documentNumber(pMessage.getDocumentNumber()).build())
                .flatMap(alertClientGateway::findAlertClient)
                .flatMap(this::restartAccumulated)
                .map(alertClient -> alertClient.toBuilder()
                        .accumulatedOperations(alertClient.getAccumulatedOperations() + 1)
                        .accumulatedAmount(alertClient.getAccumulatedAmount() + pMessage.getAmount()).build())
                .switchIfEmpty(Mono.error(new BusinessException(ALERT_CLIENT_NOT_FOUND)))
                .flatMap(alertClientGateway::accumulate)
                .filter(alertClient -> alertClient.getAccumulatedAmount() >= alertClient.getAmountEnable() ||
                        alertClient.getAccumulatedOperations() >= alertClient.getNumberOperations())
                .map(alertClient -> alert)
                .switchIfEmpty(Mono.error(new BusinessException(AMOUNT_NOT_EXCEEDED)));
    }

    public Flux<Alert> getAlertsByTransactions(Message message) {
        return Mono.just(message)
                .flatMapMany(alertGateway::findAlertByTrx)
                .flatMap(alert -> UtilParameters.validateMessageAlert(alert, message))
                .switchIfEmpty(Mono.error(new BusinessException(ALERT_TRANSACTION_NOT_FOUND)));
    }

    private Mono<Alert> buildAlertGeneral(Message message){
        return Mono.just(message)
                .filter(message1 -> !message1.getParameters().values().isEmpty())
                .switchIfEmpty(Mono.error(new BusinessException(REQUIRED_MESSAGE)))
                .flatMap(message1 -> alertGateway.findAlertById("GNR"))
                .switchIfEmpty(Mono.error(new BusinessException(ALERT_NOT_FOUND)))
                .map(alert -> alert.toBuilder()
                        .priority(message.getPriority())
                        .templateName(message.getTemplate())
                        .message(message.getParameters().entrySet().iterator().next().getValue())
                        .build());
    }

    public Mono<Alert> getAlert(Message message) {
        return Mono.just(message)
                .filter(message1 -> !message1.getAlert().isEmpty())
                .map(Message::getAlert)
                .flatMap(alertGateway::findAlertById)
                .flatMap(alert -> UtilParameters.validateMessageAlert(alert, message))
                .switchIfEmpty(buildAlertGeneral(message))
                .switchIfEmpty(Mono.error(new BusinessException(ALERT_NOT_FOUND)));
    }
}
