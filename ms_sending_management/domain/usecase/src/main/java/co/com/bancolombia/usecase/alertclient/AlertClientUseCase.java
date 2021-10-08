package co.com.bancolombia.usecase.alertclient;

import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.alert.gateways.AlertGateway;
import co.com.bancolombia.model.alertclient.AlertClient;
import co.com.bancolombia.model.alertclient.gateways.AlertClientGateway;
import co.com.bancolombia.model.client.ResponseClient;
import co.com.bancolombia.model.response.StatusResponse;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

import static co.com.bancolombia.commons.constants.Header.*;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.ALERT_CLIENT_NOT_FOUND;

@RequiredArgsConstructor
public class AlertClientUseCase {

    private final AlertClientGateway alertClientGateway;
    private final AlertGateway alertGateway;

    public Mono<ResponseClient> matchClientWithBasicKit(Map<String, String> headers){
        return alertGateway.findAlertKitBasic()
                .map(alert -> AlertClient.builder()
                        .documentType(Integer.parseInt( headers.get(DOCUMENT_TYPE)))
                        .documentNumber(Long.parseLong(headers.get(DOCUMENT_NUMBER)))
                        .associationOrigin(headers.get(ASSOCIATION_ORIGIN))
                        .idAlert(alert.getId()).numberOperations(1)
                        .amountEnable(alert.getNature().equals("MO") ? new Long(50000) : new Long(0))
                        .build())
                .flatMap(this::saveAlertClient)
                .last()
                .map(alertClient1 -> ResponseClient.builder().response(true).build());
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

    public Mono<List<AlertClient>> findAllAlertClientByClient(Map<String, String> headers) {
        return alertClientGateway.findAllAlertsByClient(headers.get(DOCUMENT_NUMBER), headers.get(DOCUMENT_TYPE))
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