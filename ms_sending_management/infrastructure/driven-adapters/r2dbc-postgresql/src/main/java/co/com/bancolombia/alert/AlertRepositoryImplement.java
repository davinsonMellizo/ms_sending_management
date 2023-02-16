package co.com.bancolombia.alert;

import co.com.bancolombia.AdapterOperations;
import co.com.bancolombia.alert.data.AlertData;
import co.com.bancolombia.alert.data.AlertMapper;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.drivenadapters.TimeFactory;
import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.alert.gateways.AlertGateway;
import co.com.bancolombia.model.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.FIND_ALERT_BY_ID_ERROR;

@Repository
public class AlertRepositoryImplement
        extends AdapterOperations<Alert, AlertData, String, AlertRepository>
        implements AlertGateway {

    @Autowired
    private TimeFactory timeFactory;

    @Autowired
    public AlertRepositoryImplement(AlertRepository repository, AlertMapper mapper) {
        super(repository, null, mapper::toEntity);
    }

    @Override
    public Mono<Alert> findAlertById(String id) {
        return repository.findAlertById(id)
                .map(this::convertToEntity)
                .onErrorMap(e -> new TechnicalException(e, FIND_ALERT_BY_ID_ERROR));
    }

    @Override
    public Flux<Alert> findAlertByTrx(Message message) {
        return repository.findAlertByTrx(message.getConsumer(), message.getTransactionCode())
                .map(this::convertToEntity)
                .onErrorMap(e -> new TechnicalException(e, FIND_ALERT_BY_ID_ERROR));
    }

}
