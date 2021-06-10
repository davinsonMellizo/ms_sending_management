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


    public Mono<StatusResponse> updateAlertRequest(Alert alert) {
        return alertGateway.findAlertById(alert.getId())
                .switchIfEmpty(Mono.error(new BusinessException(ALERT_NOT_FOUND)))
                .flatMap(alertBefore -> updateAlert(alert, alertBefore));
    }

    private Mono<StatusResponse> updateAlert(Alert alert, Alert alertBefore) {
        return alertGateway.updateAlert(alert, alertBefore)
                .map(alertActual -> StatusResponse.builder()
                        .description("Actualización de Alerta Exitosamente")
                        .before(alertBefore)
                        .actual(alertActual)
                        .build()
                );
    }

    public Mono<String> deleteAlertRequest(String id) {
        return alertGateway.findAlertById(id)
                .switchIfEmpty(Mono.error(new BusinessException(ALERT_NOT_FOUND)))
                .map(Alert::getId)
                .flatMap(alertGateway::deleteAlert);
    }
}
