package co.com.bancolombia.model.alert.gateways;

import co.com.bancolombia.model.alert.Alert;
import reactor.core.publisher.Mono;

public interface AlertGateway {
    Mono<Alert> findAlertById(String id);

    Mono<Alert> saveAlert(Alert alert);

    Mono<Alert> updateAlert(Alert alert, Alert alertBefore);

    Mono<String> deleteAlert(String id);

}
