package co.com.bancolombia.usecase.alertclient;

import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.alert.gateways.AlertGateway;
import co.com.bancolombia.model.alertclient.AlertClient;
import co.com.bancolombia.model.alertclient.gateways.AlertClientGateway;
import co.com.bancolombia.model.client.ResponseClient;
import co.com.bancolombia.model.client.gateways.ClientGateway;
import co.com.bancolombia.usecase.log.NewnessUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.List;
import java.util.Map;

import static co.com.bancolombia.commons.constants.Header.*;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.ALERT_CLIENT_NOT_FOUND;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.CLIENT_NOT_FOUND;

@RequiredArgsConstructor
public class AlertClientUseCase {

    private final AlertClientGateway alertClientGateway;
    private final NewnessUseCase newnessUseCase;
    private final ClientGateway clientGateway;
    private final AlertGateway alertGateway;

    public Mono<ResponseClient> matchClientWithBasicKit(Map<String, String> headers){
        return alertGateway.findAlertKitBasic()
                .map(alert -> AlertClient.builder()
                        .documentType(Integer.parseInt( headers.get(DOCUMENT_TYPE)))
                        .documentNumber(Long.parseLong(headers.get(DOCUMENT_NUMBER)))
                        .associationOrigin(headers.get(ASSOCIATION_ORIGIN))
                        .idAlert(alert.getId()).numberOperations(1)
                        .amountEnable(alert.getNature().equals("MO") ? Long.valueOf(50000) : Long.valueOf(0))
                        .build())
                .flatMap(this::saveAlertClient)
                .last()
                .map(alertClient1 -> ResponseClient.builder().response(true).build());
    }

    public Mono<AlertClient> saveAlertClient(AlertClient alertClient) {
        return alertClientGateway.save(alertClient)
                .flatMap(newnessUseCase::saveNewness);
    }

    public Mono<AlertClient> updateAlertClient(List<AlertClient> alertClients) {
        return alertClientGateway.findAlertsByClient(alertClients.get(0).getDocumentNumber(),
                alertClients.get(0).getDocumentType())
                .map(AlertClient::getIdAlert)
                .collectList()
                .flatMapMany(alerts->Flux.fromIterable(alertClients)
                        .map(alertClient -> Tuples.of(alerts, alertClient)))
                .flatMap(this::validateValues).last();
    }

    private Mono<AlertClient> validateValues(Tuple2<List<String>, AlertClient> data) {
        return Mono.just(data)
                .filter(data1 -> data.getT2().getAmountEnable()!=0 || data.getT2().getNumberOperations()!=0)
                .flatMap(this::validateCreation)
                .switchIfEmpty(validateDeletion(data));
    }
    private Mono<AlertClient> validateCreation(Tuple2<List<String>, AlertClient> data) {
        return Mono.just(data.getT2())
                .doOnNext(System.out::println)
                .filter(alertClient ->  data.getT1().contains(data.getT2().getIdAlert()))
                .flatMap(alertClientGateway::updateAlertClient)
                .flatMap(newnessUseCase::saveNewness)
                .switchIfEmpty(saveAlertClient(data.getT2()));
    }
    private Mono<AlertClient> validateDeletion(Tuple2<List<String>, AlertClient> data) {
        return Mono.just(data.getT2())
                .filter(alertClient ->  data.getT1().contains(data.getT2().getIdAlert()))
                .flatMap(this::deleteAlertClient);
    }

    public Mono<List<AlertClient>> findAllAlertClientByClient(Map<String, String> headers) {
        return clientGateway.findClientByIdentification(Long.parseLong(headers.get(DOCUMENT_NUMBER)),
                Integer.parseInt(headers.get(DOCUMENT_TYPE)))
                .switchIfEmpty(Mono.error(new BusinessException(CLIENT_NOT_FOUND)))
                .flatMapMany(client -> alertClientGateway.alertsVisibleChannelByClient(client.getDocumentNumber(),
                        client.getIdDocumentType()))
                .map(alertClient -> alertClient.toBuilder()
                .documentNumber(Long.parseLong(headers.get(DOCUMENT_NUMBER)))
                .documentType(Integer.parseInt(headers.get(DOCUMENT_TYPE))).build())
                .switchIfEmpty(Mono.error(new BusinessException(ALERT_CLIENT_NOT_FOUND)))
                .collectList();
    }

    public Mono<AlertClient> deleteAlertClient(AlertClient alertClient) {
        return alertClientGateway.delete(alertClient)
                .flatMap(newnessUseCase::saveNewness)
                .switchIfEmpty(Mono.error(new BusinessException(ALERT_CLIENT_NOT_FOUND)));
    }

}