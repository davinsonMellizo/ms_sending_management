package co.com.bancolombia.alertclient;

import co.com.bancolombia.AdapterOperations;
import co.com.bancolombia.alertclient.data.AlertClientData;
import co.com.bancolombia.alertclient.data.AlertClientMapper;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.drivenadapters.TimeFactory;
import co.com.bancolombia.model.alertclient.AlertClient;
import co.com.bancolombia.model.alertclient.gateways.AlertClientGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.*;

@Repository
public class AlertClientRepositoryImplement
        extends AdapterOperations<AlertClient, AlertClientData, String, AlertClientRepository>
        implements AlertClientGateway {

    private static final int NUMBER_ZERO = 0;
    private static final int NUMBER_ONE = 1;

    @Autowired
    private TimeFactory timeFactory;

    @Autowired
    public AlertClientRepositoryImplement(AlertClientRepository repository, AlertClientMapper mapper) {
        super(repository, mapper::toData, mapper::toEntity);
    }

    @Override
    public Mono<AlertClient> save(AlertClient alertClient) {
        return Mono.just(alertClient)
                .map(this::convertToData)
                .map(data -> data.toBuilder().createdDate(timeFactory.now())
                        .accumulatedAmount(Long.valueOf(NUMBER_ZERO))
                        .accumulatedOperations(NUMBER_ZERO)
                        .modifiedDate(timeFactory.now()).transactionDate(timeFactory.now()).build())
                .flatMap(repository::save)
                .map(this::convertToEntity)
                .onErrorMap(e -> new TechnicalException(e, SAVE_ALERT_CLIENT_ERROR));
    }

    @Override
    public Mono<AlertClient> updateAlertClient(AlertClient alertClient) {
        return repository.updateAlertClient(alertClient.getNumberOperations(),
                alertClient.getAmountEnable(),
                alertClient.getIdAlert(),
                alertClient.getDocumentNumber(),
                alertClient.getDocumentType())
                .filter(rowsAffected -> rowsAffected == NUMBER_ONE)
                .map(integer -> alertClient)
                .onErrorMap(e -> new TechnicalException(e, UPDATE_ALERT_CLIENT_ERROR));
    }

    @Override
    public Mono<AlertClient> accumulate(AlertClient alertClient) {
        return repository.accumulate(alertClient.getAccumulatedOperations(),
                alertClient.getAccumulatedAmount(),
                alertClient.getIdAlert(),
                alertClient.getDocumentNumber(),
                alertClient.getDocumentType(),
                timeFactory.now())
                .filter(rowsAffected -> rowsAffected == NUMBER_ONE)
                .map(integer -> alertClient)
                .onErrorMap(e -> new TechnicalException(e, ACCUMULATE_ALERT_CLIENT_ERROR));
    }

    @Override
    public Flux<AlertClient> alertsVisibleChannelByClient(Long documentNumber, Integer documentType) {
        return repository.alertsClientVisibleChannelByClient(documentNumber, documentType)
                .map(this::convertToEntity)
                .onErrorMap(e -> new TechnicalException(e, FIND_ALL_ALERT_CLIENT_ERROR));
    }

    @Override
    public Mono<AlertClient> delete(AlertClient alertClient) {
        return repository.deleteAlertClient(alertClient.getIdAlert(), alertClient.getDocumentNumber(),
                        alertClient.getDocumentType())
                .filter(rowsAffected -> rowsAffected == NUMBER_ONE)
                .map(integer -> alertClient)
                .onErrorMap(e -> new TechnicalException(e, DELETE_ALERT_CLIENT_ERROR));
    }

    @Override
    public Mono<AlertClient> findAlertClient(AlertClient alertClient) {
        return repository.findAlertClient(alertClient.getIdAlert(), alertClient.getDocumentNumber(),
                        alertClient.getDocumentType())
                .map(this::convertToEntity)
                .onErrorMap(e -> new TechnicalException(e, FIND_ALERT_CLIENT_ERROR));
    }

    @Override
    public Flux<AlertClient> findAlertsByClient(Long documentNumber, Integer documentType) {
        return repository.findAlertsByClient(documentNumber, documentType)
                .map(this::convertToEntity)
                .onErrorMap(e -> new TechnicalException(e, FIND_ALERT_CLIENT_ERROR));
    }

}
