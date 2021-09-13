package co.com.bancolombia.model.alertclient.gateways;

import co.com.bancolombia.model.alertclient.AlertClient;
import co.com.bancolombia.model.response.StatusResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AlertClientGateway {

    Mono<AlertClient> save(AlertClient alertClient);

    Mono<StatusResponse<AlertClient>> updateAlertClient(StatusResponse<AlertClient> statusResponse);

    Flux<AlertClient> findAllAlertsByClient(Integer idAlertClient);

    Mono<String> delete(AlertClient alertClient);

    Mono<AlertClient> findAlertClient(AlertClient alertClient);

}
