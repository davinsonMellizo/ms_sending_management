package co.com.bancolombia.usecase.log;

import co.com.bancolombia.model.alert.gateways.AlertGateway;
import co.com.bancolombia.model.alertclient.AlertClient;
import co.com.bancolombia.model.newness.Newness;
import co.com.bancolombia.model.newness.gateways.NewnessRepository;
import co.com.bancolombia.usecase.commons.FactoryLog;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

@RequiredArgsConstructor
public class NewnessUseCase {
    private final NewnessRepository newnessRepository;
    private final AlertGateway alertGateway;

    private static final Predicate<AlertClient> isNotNullDescription = alertClient ->
            (Objects.isNull(alertClient.getAlertDescription()) || alertClient.getAlertDescription().isEmpty());

    public Mono<AlertClient> saveNewness(List<AlertClient> pAlertClients, String transaction) {
        return Flux.fromIterable(pAlertClients)
                .flatMap(this::findAlert)
                .flatMap(alertClient -> FactoryLog.createLog(alertClient, transaction))
                .collectList()
                .flatMap(newnessRepository::saveNewness)
                .onErrorReturn(new Newness())
                .thenReturn(pAlertClients.get(0));
    }

    private Mono<AlertClient> findAlert(AlertClient alertClient) {
        return Mono.just(alertClient)
                .filter(isNotNullDescription)
                .map(AlertClient::getIdAlert)
                .flatMap(alertGateway::findAlertById)
                .map(alert -> alertClient.toBuilder().alertDescription(alert.getDescription()).build())
                .switchIfEmpty(Mono.just(alertClient));

    }
}
