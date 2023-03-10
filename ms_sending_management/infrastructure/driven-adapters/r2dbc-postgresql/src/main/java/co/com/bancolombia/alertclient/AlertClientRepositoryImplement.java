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
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.ACCUMULATE_ALERT_CLIENT_ERROR;
import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.FIND_ALERT_CLIENT_ERROR;

@Repository
public class AlertClientRepositoryImplement
        extends AdapterOperations<AlertClient, AlertClientData, String, AlertClientRepository>
        implements AlertClientGateway {

    private static final int NUMBER_ONE = 1;

    @Autowired
    private TimeFactory timeFactory;

    @Autowired
    public AlertClientRepositoryImplement(AlertClientRepository repository, AlertClientMapper mapper) {
        super(repository, null, mapper::toEntity);
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
    public Mono<AlertClient> findAlertClient(AlertClient alertClient) {
        return repository.findAlertClient(alertClient.getIdAlert(), alertClient.getDocumentNumber(),
                        alertClient.getDocumentType())
                .map(this::convertToEntity)
                .onErrorMap(e -> new TechnicalException(e, FIND_ALERT_CLIENT_ERROR));
    }

}
