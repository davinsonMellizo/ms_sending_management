package co.com.bancolombia.model.alertclient.gateways;

import co.com.bancolombia.model.alertclient.AlertClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface AlertClientGateway {

    Mono<AlertClient> save(List<AlertClient> alertClient);

    Mono<AlertClient> updateAlertClient(AlertClient alertClient);

    Mono<AlertClient> accumulate(AlertClient alertClient);

    Flux<AlertClient> alertsVisibleChannelByClient(Long documentNumber, Integer documentType);

    Mono<AlertClient> delete(AlertClient alertClient);

    Mono<AlertClient> findAlertClient(AlertClient alertClient);

    Flux<AlertClient> findAlertsByClient(Long documentNumber, Integer documentType);

}
