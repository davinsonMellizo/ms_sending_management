package co.com.bancolombia.model.contact.gateways;

import co.com.bancolombia.model.client.Client;
import co.com.bancolombia.model.contact.Contact;
import co.com.bancolombia.model.response.StatusResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ContactGateway {
    Flux<Contact> findAllContactsByClient(Client client);

    Flux<Contact> findIdContact(Contact contact);

    Mono<Contact> saveContact(Contact contact);

    Mono<Contact> updateContact(Contact contact);

    Mono<Integer> deleteContact(Integer id);

}
