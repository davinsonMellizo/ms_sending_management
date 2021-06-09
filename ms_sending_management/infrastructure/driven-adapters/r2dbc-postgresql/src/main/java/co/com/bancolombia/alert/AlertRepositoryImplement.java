package co.com.bancolombia.alert;

import co.com.bancolombia.AdapterOperations;
import co.com.bancolombia.alert.data.AlertData;
import co.com.bancolombia.alert.data.AlertMapper;
import co.com.bancolombia.drivenadapters.TimeFactory;
import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.alert.gateways.AlertGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

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
        return findById(id);
    }

    @Override
    public Mono<Alert> saveAlert(Alert alert) {
        return Mono.just(alert)
                .map(this::convertToData)
                .map(alertData ->alertData.toBuilder()
                        .newAlert(true)
                        .createdDate(timeFactory.now())
                        .build())
                .flatMap(this::saveData)
                .map(this::convertToEntity);
    }

    @Override
    public Mono<Alert> updateAlert(Alert alert) {
        return
        save(alert);
    }

    @Override
    public Mono<String> deleteAlert(String id) {
        return deleteById(id)
                .thenReturn(id);
    }
}
