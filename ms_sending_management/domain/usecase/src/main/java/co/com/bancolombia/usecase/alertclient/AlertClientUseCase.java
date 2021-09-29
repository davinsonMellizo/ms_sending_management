package co.com.bancolombia.usecase.alertclient;

import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.alert.gateways.AlertGateway;
import co.com.bancolombia.model.alertclient.AlertClient;
import co.com.bancolombia.model.alertclient.gateways.AlertClientGateway;
import co.com.bancolombia.model.response.StatusResponse;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.List;

import static co.com.bancolombia.commons.enums.BusinessErrorMessage.ALERT_CLIENT_NOT_FOUND;

@RequiredArgsConstructor
public class AlertClientUseCase {

    private final AlertClientGateway alertClientGateway;
    private final AlertGateway alertGateway;

    public Mono<Boolean> matchClientWithBasicKit(AlertClient alertClient){
        return alertGateway.findAlertKitBasic()
                .map(alert -> alertClient.toBuilder()
                        .idAlert(alert.getId()).numberOperations(1)
                        .amountEnable(alert.getNature().equals("MO")?new Long(50000):new Long(0))
                        .build())
                .flatMap(this::saveAlertClient)
                .last()
                .map(alertClientSaved -> true);
    }

    public Mono<AlertClient> saveAlertClient(AlertClient alertClient) {
        return alertClientGateway.save(alertClient);
    }

    public Mono<StatusResponse<AlertClient>> updateAlertClient(AlertClient alertClient) {
        return alertClientGateway.findAlertClient(alertClient)
                .switchIfEmpty(Mono.error(new BusinessException(ALERT_CLIENT_NOT_FOUND)))
                .map(alertCliBefore -> buildResponse(alertCliBefore, alertClient))
                .flatMap(alertClientGateway::updateAlertClient);
    }

    public Mono<List<AlertClient>> findAllAlertClient(Integer idAlertClient) {
        return alertClientGateway.findAllAlertsByClient(idAlertClient)
                .switchIfEmpty(Mono.error(new BusinessException(ALERT_CLIENT_NOT_FOUND)))
                .collectList();
    }

    private StatusResponse<AlertClient> buildResponse(AlertClient before, AlertClient actual) {
        return StatusResponse.<AlertClient>builder()
                .before(before)
                .actual(actual)
                .description("Actualizado exitosamente")
                .build();
    }

    public Mono<String> deleteAlertClient(AlertClient client) {
        return alertClientGateway.delete(client)
                .switchIfEmpty(Mono.error(new BusinessException(ALERT_CLIENT_NOT_FOUND)));
    }

}