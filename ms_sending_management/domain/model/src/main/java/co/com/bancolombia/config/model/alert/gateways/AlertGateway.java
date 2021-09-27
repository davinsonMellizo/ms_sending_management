package co.com.bancolombia.config.model.alert.gateways;

import co.com.bancolombia.config.model.alert.Alert;
import co.com.bancolombia.config.model.response.StatusResponse;
import reactor.core.publisher.Mono;

public interface AlertGateway {
    Mono<Alert> findAlertById(String id);

    Mono<Alert> saveAlert(Alert alert);

    Mono<StatusResponse<Alert>> updateAlert(Alert alert);

    Mono<String> deleteAlert(String id);

}
