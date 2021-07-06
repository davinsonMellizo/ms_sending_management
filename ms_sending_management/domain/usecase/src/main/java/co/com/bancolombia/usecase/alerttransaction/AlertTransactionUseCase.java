package co.com.bancolombia.usecase.alerttransaction;

import co.com.bancolombia.commons.enums.BusinessErrorMessage;
import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.alerttransaction.AlertTransaction;
import co.com.bancolombia.model.alerttransaction.gateways.AlertTransactionGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.List;

import static co.com.bancolombia.commons.enums.BusinessErrorMessage.ALERT_TRANSACTION_NOT_FOUND;


@RequiredArgsConstructor
public class AlertTransactionUseCase {
    private final AlertTransactionGateway alertTransactionGateway;

    public Mono<AlertTransaction> saveAlertTransaction(AlertTransaction alertTransaction) {
        return alertTransactionGateway.saveAlertTransaction(alertTransaction);
    }

    public Mono<String> deleteAlertTransaction(AlertTransaction alertTransaction) {
        return alertTransactionGateway.deleteAlertTransaction(alertTransaction)
                .switchIfEmpty(Mono.error(new BusinessException(ALERT_TRANSACTION_NOT_FOUND)));
    }

    public Mono<List<AlertTransaction>> findAllAlertTransaction(String idAlert) {
        return alertTransactionGateway.findAllAlertTransaction(idAlert)
                .switchIfEmpty(Mono.error(new BusinessException(ALERT_TRANSACTION_NOT_FOUND)))
                .collectList();
    }
}
