package co.com.bancolombia.alertclient;

import co.com.bancolombia.AdapterOperations;
import co.com.bancolombia.alertclient.data.AlertClientData;
import co.com.bancolombia.alertclient.data.AlertClientMapper;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.model.alertclient.AlertClient;
import co.com.bancolombia.model.alertclient.gateways.AlertClientGateway;
import co.com.bancolombia.model.response.StatusResponse;
import co.com.bancolombia.drivenadapters.TimeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.*;

@Repository
public class AlertClientRepositoryImplement
        extends AdapterOperations<AlertClient, AlertClientData, String, AlertClientRepository>
        implements AlertClientGateway {

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
                        .modifiedDate(timeFactory.now()).transactionDate(timeFactory.now()).build())
                .flatMap(repository::save)
                .map(this::convertToEntity)
                .onErrorMap(e -> new TechnicalException(e, SAVE_ALERT_CLIENT_ERROR));
    }

    @Override
    public Mono<StatusResponse<AlertClient>> updateAlertClient(StatusResponse<AlertClient> statusResponse) {
        return repository.updateAlertClient(statusResponse.getActual().getNumberOperations(),
                statusResponse.getActual().getAmountEnable(),
                statusResponse.getActual().getIdAlert(),
                statusResponse.getActual().getIdClient())
                .filter(rowsAffected -> rowsAffected == NUMBER_ONE)
                .map(integer -> statusResponse)
                .onErrorMap(e -> new TechnicalException(e, UPDATE_ALERT_CLIENT_ERROR));
    }

    @Override
    public Flux<AlertClient> findAllAlertsByClient(Integer idAlertClient) {
        return repository.findAllAlertsByClient(idAlertClient)
                .map(this::convertToEntity)
                .onErrorMap(e -> new TechnicalException(e, FIND_ALL_ALERT_CLIENT_ERROR));
    }

    @Override
    public Mono<String> delete(AlertClient alertClient) {
        return repository.deleteAlertClient(alertClient.getIdAlert(),
                alertClient.getIdClient())
                .filter(rowsAffected -> rowsAffected == NUMBER_ONE)
                .map(integer -> alertClient.getIdAlert())
                .onErrorMap(e -> new TechnicalException(e, DELETE_ALERT_CLIENT_ERROR));
    }

    @Override
    public Mono<AlertClient> findAlertClient(AlertClient alertClient) {
        return repository.findAlertClient(alertClient.getIdAlert(), alertClient.getIdClient())
                .map(this::convertToEntity)
                .onErrorMap(e -> new TechnicalException(e, FIND_ALERT_CLIENT_ERROR));
    }

}
