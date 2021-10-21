package co.com.bancolombia.usecase.log;

import co.com.bancolombia.model.alertclient.AlertClient;
import co.com.bancolombia.model.client.Client;
import co.com.bancolombia.model.contact.Contact;
import co.com.bancolombia.model.newness.Newness;
import co.com.bancolombia.model.newness.gateways.NewnessRepository;
import co.com.bancolombia.usecase.commons.FactoryLog;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class NewnessUseCase {
    private final NewnessRepository newnessRepository;

    public Mono<AlertClient> saveNewness(AlertClient alertClient){
        return Mono.just(alertClient)
                .flatMap(FactoryLog::createLog)
                .flatMap(newnessRepository::saveNewness)
                .onErrorReturn(new Newness())
                .thenReturn(alertClient);
    }
}
