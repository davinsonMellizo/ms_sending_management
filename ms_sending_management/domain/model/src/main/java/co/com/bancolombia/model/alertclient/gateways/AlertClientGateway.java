package co.com.bancolombia.model.alertclient.gateways;

import co.com.bancolombia.model.alertclient.AlertClient;
import reactor.core.publisher.Mono;

public interface AlertClientGateway {

    Mono<AlertClient> accumulate(AlertClient alertClient);

    Mono<AlertClient> findAlertClient(AlertClient alertClient);

}
