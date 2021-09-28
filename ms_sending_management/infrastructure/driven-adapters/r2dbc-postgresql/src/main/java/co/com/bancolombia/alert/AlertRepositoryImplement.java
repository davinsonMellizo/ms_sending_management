package co.com.bancolombia.alert;

import co.com.bancolombia.AdapterOperations;
import co.com.bancolombia.alert.data.AlertData;
import co.com.bancolombia.alert.data.AlertMapper;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.alert.gateways.AlertGateway;
import co.com.bancolombia.model.response.StatusResponse;
import co.com.bancolombia.drivenadapters.TimeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.*;

@Repository
public class AlertRepositoryImplement
        extends AdapterOperations<Alert, AlertData, String, AlertRepository>
        implements AlertGateway {

    @Autowired
    private TimeFactory timeFactory;

    @Autowired
    public AlertRepositoryImplement(AlertRepository repository, AlertMapper mapper) {
        super(repository, mapper::toData, mapper::toEntity);
    }

    @Override
    public Mono<Alert> findAlertById(String id) {
        return doQuery(repository.findById(id))
                .onErrorMap(e -> new TechnicalException(e, FIND_ALERT_BY_ID_ERROR));
    }

    @Override
    public Mono<Alert> saveAlert(Alert alert) {
        return Mono.just(alert)
                .map(this::convertToData)
                .map(alertData -> alertData.toBuilder()
                        .isNew(true)
                        .createdDate(timeFactory.now())
                        .build())
                .flatMap(repository::save)
                .map(this::convertToEntity)
                .onErrorMap(e -> new TechnicalException(e, SAVE_ALERT_ERROR));
    }

    @Override
    public Mono<StatusResponse<Alert>> updateAlert(Alert alert) {
        return findAlertById(alert.getId())
                .map(alertFound -> StatusResponse.<Alert>builder()
                        .before(alertFound)
                        .actual(alert)
                        .build())
                .flatMap(this::update);
    }

    private Mono<StatusResponse<Alert>> update(StatusResponse<Alert> response) {
        return Mono.just(response.getActual())
                .map(this::convertToData)
                .map(data -> data.toBuilder().createdDate(response.getBefore().getCreatedDate()).isNew(false).build())
                .flatMap(repository::save)
                .map(this::convertToEntity)
                .map(actual -> response.toBuilder().actual(actual).description("Actualización Exitosa").build())
                .onErrorMap(e -> new TechnicalException(e, UPDATE_ALERT_ERROR));
    }

    @Override
    public Mono<String> deleteAlert(String id) {
        return repository.deleteById(id)
                .onErrorMap(e -> new TechnicalException(e, DELETE_ALERT_ERROR))
                .thenReturn(id);
    }
}
