package co.com.bancolombia.model.alerttransaction.gateways;

import co.com.bancolombia.model.alerttransaction.AlertTransaction;
import co.com.bancolombia.model.message.Message;
import reactor.core.publisher.Flux;

public interface AlertTransactionGateway {
    Flux<AlertTransaction> findAllAlertTransaction(Message message);
}
