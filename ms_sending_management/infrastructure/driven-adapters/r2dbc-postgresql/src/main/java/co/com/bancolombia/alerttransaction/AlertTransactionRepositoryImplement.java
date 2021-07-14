package co.com.bancolombia.alerttransaction;


import co.com.bancolombia.AdapterOperations;
import co.com.bancolombia.alerttransaction.data.AlertTransactionData;
import co.com.bancolombia.alerttransaction.data.AlertTransactionMapper;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.drivenadapters.TimeFactory;
import co.com.bancolombia.model.alerttransaction.AlertTransaction;
import co.com.bancolombia.model.alerttransaction.gateways.AlertTransactionGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.*;

@Repository
public class AlertTransactionRepositoryImplement
        extends AdapterOperations<AlertTransaction, AlertTransactionData, String, AlertTransactionRepository>
        implements AlertTransactionGateway {

    @Autowired
    private TimeFactory timeFactory;
    private static final int NUMBER_OF_ROWS = 1;

    @Autowired
    public AlertTransactionRepositoryImplement(AlertTransactionRepository repository, AlertTransactionMapper mapper) {
        super(repository, mapper::toData, mapper::toEntity);
    }


    @Override
    public Mono<AlertTransaction> saveAlertTransaction(AlertTransaction alertTransaction) {
        return Mono.just(alertTransaction)
                .map(this::convertToData)
                .map(data -> data.toBuilder().createdDate(timeFactory.now()).build())
                .flatMap(repository::save)
                .map(this::convertToEntity)
                .onErrorMap(e -> new TechnicalException(e, SAVE_ALERT_TRANSACTION_ERROR));
    }

    @Override
    public Mono<String> deleteAlertTransaction(AlertTransaction alertTransaction) {
        return repository.deleteAlertTransaction(alertTransaction.getIdAlert(),
                alertTransaction.getIdConsumer(), alertTransaction.getIdTransaction())
                .filter(rowsAffected -> rowsAffected == NUMBER_OF_ROWS)
                .map(integer -> alertTransaction.getIdAlert())
                .onErrorMap(e -> new TechnicalException(e, DELETE_ALERT_TRANSACTION_ERROR));
    }

    @Override
    public Flux<AlertTransaction> findAllAlertTransaction(String idAlert) {
        return repository.findAllAlertTransaction(idAlert)
                .map(this::convertToEntity)
                .onErrorMap(e -> new TechnicalException(e, FIND_ALL_ALERT_TRANSACTION_ERROR));
    }

    @Override
    public Flux<AlertTransaction> findAllAlertTransaction(String idTrx, String idConsumer) {
        return null;
    }
}
