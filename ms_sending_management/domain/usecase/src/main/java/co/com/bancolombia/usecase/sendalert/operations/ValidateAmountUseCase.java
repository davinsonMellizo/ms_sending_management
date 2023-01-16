package co.com.bancolombia.usecase.sendalert.operations;

import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.alertclient.AlertClient;
import co.com.bancolombia.model.alertclient.gateways.AlertClientGateway;
import co.com.bancolombia.model.message.Message;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

import static co.com.bancolombia.commons.enums.BusinessErrorMessage.ALERT_CLIENT_NOT_FOUND;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.AMOUNT_NOT_EXCEEDED;

@RequiredArgsConstructor
public class ValidateAmountUseCase {
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

    public Mono<Alert> validateAmount(Alert alert, Message message) {
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
}
