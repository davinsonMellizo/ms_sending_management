package co.com.bancolombia.usecase.log;

import co.com.bancolombia.model.alert.gateways.AlertGateway;
import co.com.bancolombia.model.alertclient.AlertClient;
import co.com.bancolombia.model.alertclient.gateways.AlertClientGateway;
import co.com.bancolombia.model.client.Client;
import co.com.bancolombia.model.contact.Contact;
import co.com.bancolombia.model.message.Message;
import co.com.bancolombia.model.newness.Newness;
import co.com.bancolombia.model.newness.gateways.NewnessRepository;
import co.com.bancolombia.usecase.commons.FactoryLog;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.function.Predicate;

@RequiredArgsConstructor
public class NewnessUseCase {
    private final NewnessRepository newnessRepository;
    private final AlertGateway alertGateway;

    private static final Predicate<AlertClient> isNotNullDescription = alertClient ->
            (Objects.isNull(alertClient.getAlertDescription()) || alertClient.getAlertDescription().isEmpty());

    public Mono<AlertClient> saveNewness(AlertClient pAlertClient, String transaction){
        return findAlert(pAlertClient)
                .switchIfEmpty(Mono.just(pAlertClient))
                .flatMap(alertClient -> FactoryLog.createLog(alertClient, transaction))
                .flatMap(newnessRepository::saveNewness)
                .onErrorReturn(new Newness())
                .thenReturn(pAlertClient);
    }

    private Mono<AlertClient> findAlert(AlertClient alertClient){
        return Mono.just(alertClient)
                .filter(isNotNullDescription)
                .map(AlertClient::getIdAlert)
                .flatMap(alertGateway::findAlertById)
                .map(alert -> alertClient.toBuilder().alertDescription(alert.getDescription()).build());

    }
}
