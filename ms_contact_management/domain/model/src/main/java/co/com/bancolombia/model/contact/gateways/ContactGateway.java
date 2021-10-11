package co.com.bancolombia.model.contact.gateways;

import co.com.bancolombia.model.client.Client;
import co.com.bancolombia.model.contact.Contact;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ContactGateway {
    Flux<Contact> contactsByClient(Client client);

    Flux<Contact> contactsByClientAndSegment(Client client, String segment);

    Flux<Contact> findIdContact(Contact contact);

    Mono<Contact> saveContact(Contact contact);

    Mono<Contact> updateContact(Contact contact);

    Mono<Integer> deleteContact(Integer id);

}
