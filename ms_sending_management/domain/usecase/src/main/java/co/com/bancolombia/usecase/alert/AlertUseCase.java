package co.com.bancolombia.usecase.alert;

import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.alert.gateways.AlertGateway;
import co.com.bancolombia.model.log.Log;
import co.com.bancolombia.model.log.gateways.LogGateway;
import co.com.bancolombia.model.response.StatusResponse;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.enums.BusinessErrorMessage.ALERT_NOT_FOUND;

@RequiredArgsConstructor
public class AlertUseCase {
    private final AlertGateway alertGateway;
    private final LogGateway logGateway;

    public Mono<Alert> findAlertByIdRequest(String id) {
        Log log = Log.builder()
                .applicationCode("SENDING")
                .accountNumber(1111)
                .accountType("h")
                .alertDestination("destino")
                .alertIndicator(1)
                .alertType("t")
                .documentNumber(new Long(111))
                .documentType(1)
                .idAlert("1")
                .enabledAmount(new Long(555))
                .userField1("uno")
                .userField2("dos")
                .userField3(new Long(3))
                .userField4(new Long(4))
                .userField5("5")
                .userField6("6")
                .messageSent("ddd")
                .messageType("d")
                .priority(1)
                .operationChannel("ch")
                .operationCode("d")
                .operationNumber(1)
                .operationDescription("ddd")
                .indicatorDescription("dd")
                .sendResponseCode(12)
                .sendResponseDescription("ddd")
                .template("ddd")
                .build();
        return logGateway.putLogToSQS(log)
                .flatMap(logs -> alertGateway.findAlertById(id))
                .switchIfEmpty(Mono.error(new BusinessException(ALERT_NOT_FOUND)));
        /*return alertGateway.findAlertById(id)
                .switchIfEmpty(Mono.error(new BusinessException(ALERT_NOT_FOUND)));*/
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
