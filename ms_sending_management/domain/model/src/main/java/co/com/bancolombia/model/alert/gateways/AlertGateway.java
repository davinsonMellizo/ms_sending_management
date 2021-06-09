package co.com.bancolombia.model.alert.gateways;

import co.com.bancolombia.model.alert.Alert;
import reactor.core.publisher.Mono;

public interface AlertGateway {
    Mono<Alert> findAlertById(Integer id);

    Mono<Alert> saveAlert(Alert alert);

    Mono<Alert> updateAlert(Alert alert);

    Mono<Integer> deleteAlert(Integer id);

}
