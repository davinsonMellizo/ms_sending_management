package co.com.bancolombia.model.alerttransaction.gateways;

import co.com.bancolombia.model.alerttransaction.AlertTransaction;
import co.com.bancolombia.model.message.Message;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AlertTransactionGateway {
    Mono<AlertTransaction> saveAlertTransaction(AlertTransaction alertTransaction);

    Mono<String> deleteAlertTransaction(AlertTransaction alertTransaction);

    Flux<AlertTransaction> findAllAlertTransaction(String idAlert);

    Flux<AlertTransaction> findAllAlertTransaction(Message message);
}
