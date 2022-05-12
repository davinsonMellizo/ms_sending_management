package co.com.bancolombia.model.client.gateways;

import co.com.bancolombia.model.client.Client;
import co.com.bancolombia.model.contact.ResponseContacts;
import reactor.core.publisher.Mono;

public interface ClientGateway {
    Mono<Boolean> matchClientWithBasicKit(Client client);

    Mono<ResponseContacts> retrieveAlertInformation(Client client);
}
