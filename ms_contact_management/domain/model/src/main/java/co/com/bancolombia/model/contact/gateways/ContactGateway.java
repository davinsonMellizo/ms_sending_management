package co.com.bancolombia.model.contact.gateways;

import co.com.bancolombia.model.client.Client;
import co.com.bancolombia.model.contact.Contact;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ContactGateway {
    Mono<List<Contact>> contactsByClient(Client client);

    Mono<List<Contact>> contactsByClientAndSegment(Client client, String segment);

    Flux<Contact> findContactsByClientSegmentAndMedium(Contact contact);

    Mono<Contact> saveContact(Contact contact);

    Mono<Contact> updateContact(Contact contact);

    Mono<Contact> deleteContact(Contact contact);

}
