package co.com.bancolombia.model.alert.gateways;

import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.response.StatusResponse;
import reactor.core.publisher.Mono;

public interface AlertGateway {
    Mono<Alert> findAlertById(String id);

    Mono<Alert> saveAlert(Alert alert);

    Mono<StatusResponse<Alert>> updateAlert(Alert alert);

    Mono<String> deleteAlert(String id);

}
