package co.com.bancolombia.usecase.log;

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

    public Mono<Client> saveNewness(Client client){
        return Mono.just(client)
                .flatMap(FactoryLog::createLog)
                .flatMap(newnessRepository::saveNewness)
                .onErrorReturn(new Newness())
                .thenReturn(client);
    }

    public Mono<Contact> saveNewness(Contact contact){
        return Mono.just(contact)
                .flatMap(FactoryLog::createLog)
                .flatMap(newnessRepository::saveNewness)
                .onErrorReturn(new Newness())
                .thenReturn(contact);
    }
}
