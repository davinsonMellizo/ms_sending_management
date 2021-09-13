package co.com.bancolombia.usecase.alert;

import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.alert.gateways.AlertGateway;
import co.com.bancolombia.model.response.StatusResponse;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.enums.BusinessErrorMessage.ALERT_NOT_FOUND;

@RequiredArgsConstructor
public class AlertUseCase {
    private final AlertGateway alertGateway;

    public Mono<Alert> findAlertByIdRequest(String id) {
        return alertGateway.findAlertById(id)
                .switchIfEmpty(Mono.error(new BusinessException(ALERT_NOT_FOUND)));
    }

    public Mono<Alert> saveAlertRequest(Alert alert) {
        return alertGateway.saveAlert(alert);
    }


    public Mono<StatusResponse<Alert>> updateAlertRequest(Alert alert) {
        return alertGateway.updateAlert(alert)
                .switchIfEmpty(Mono.error(new BusinessException(ALERT_NOT_FOUND)));
    }

    public Mono<String> deleteAlertRequest(String id) {
        return alertGateway.findAlertById(id)
                .switchIfEmpty(Mono.error(new BusinessException(ALERT_NOT_FOUND)))
                .map(Alert::getId)
                .flatMap(alertGateway::deleteAlert);
    }
}
