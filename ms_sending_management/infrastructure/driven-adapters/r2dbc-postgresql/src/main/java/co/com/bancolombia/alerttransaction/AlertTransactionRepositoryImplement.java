package co.com.bancolombia.alerttransaction;


import co.com.bancolombia.AdapterOperations;
import co.com.bancolombia.alerttransaction.data.AlertTransactionData;
import co.com.bancolombia.alerttransaction.data.AlertTransactionMapper;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.model.alerttransaction.AlertTransaction;
import co.com.bancolombia.model.alerttransaction.gateways.AlertTransactionGateway;
import co.com.bancolombia.model.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.FIND_ALL_ALERT_TRANSACTION_ERROR;

@Repository
public class AlertTransactionRepositoryImplement
        extends AdapterOperations<AlertTransaction, AlertTransactionData, String, AlertTransactionRepository>
        implements AlertTransactionGateway {

    @Autowired
    public AlertTransactionRepositoryImplement(AlertTransactionRepository repository, AlertTransactionMapper mapper) {
        super(repository, null, mapper::toEntity);
    }


    @Override
    public Flux<AlertTransaction> findAllAlertTransaction(Message message) {
        return repository.findAllAlertTransaction(message.getConsumer(), message.getTransactionCode())
                .map(this::convertToEntity)
                .onErrorMap(e -> new TechnicalException(e, FIND_ALL_ALERT_TRANSACTION_ERROR));
    }
}
