package co.com.bancolombia.usecase.alert;

import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.alert.gateways.AlertGateway;
import co.com.bancolombia.model.response.ContactsResponse;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class AlertUseCase {
    private final AlertGateway alertGateway;

    public Mono<ContactsResponse> findAlertById(Integer id) {
        return null;
    }

    public Mono<Alert> saveAlert(Alert alert) {
        return null;
    }


    public Mono<ContactsResponse> updateAlert(Alert alert) {
        return null;
    }

    public Mono<Integer> deleteAlert(Alert alert) {
        return null;
    }
}
