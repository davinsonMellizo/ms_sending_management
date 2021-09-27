package co.com.bancolombia.usecase.contact;

import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.client.Client;
import co.com.bancolombia.model.consumer.Consumer;
import co.com.bancolombia.model.consumer.gateways.ConsumerGateway;
import co.com.bancolombia.model.contact.Contact;
import co.com.bancolombia.model.contact.ResponseContacts;
import co.com.bancolombia.model.contact.gateways.ContactGateway;
import co.com.bancolombia.model.contactmedium.ContactMedium;
import co.com.bancolombia.model.contactmedium.gateways.ContactMediumGateway;
import co.com.bancolombia.model.document.Document;
import co.com.bancolombia.model.document.gateways.DocumentGateway;
import co.com.bancolombia.model.response.StatusResponse;
import co.com.bancolombia.model.state.State;
import co.com.bancolombia.model.state.gateways.StateGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple3;
import reactor.util.function.Tuple4;


import static co.com.bancolombia.commons.enums.BusinessErrorMessage.CONTACT_NOT_FOUND;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.INVALID_DATA;

@RequiredArgsConstructor
public class ContactUseCase {
    private final StateGateway stateGateway;
    private final ContactGateway contactGateway;
    private final DocumentGateway documentGateway;
    private final ConsumerGateway consumerGateway;
    private final ContactMediumGateway contactMediumGateway;

    public Mono<ResponseContacts> findContactsByClient(Client client) {
        return contactGateway.findAllContactsByClient(client)
                .collectList()
                .map(contacts -> ResponseContacts.<Contact>builder()
                        .contacts(contacts)
                        .documentNumber(client.getDocumentNumber())
                        .documentType(client.getDocumentType())
                        .build());
    }

    public Mono<Contact> saveContact(Contact contact) {
        return Mono.just(contact)
                .flatMap(this::getDataBase)
                .switchIfEmpty(Mono.error(new BusinessException(INVALID_DATA)))
                .map(data -> contact.toBuilder()
                        .state(Integer.toString(data.getT1().getId()))
                        .contactMedium(Integer.toString(data.getT2().getId()))
                        .documentType(data.getT3().getId())
                        .segment(data.getT4().getSegment())
                        .build())
                .flatMap(contactGateway::saveContact);
    }

    private Mono<Tuple4<State, ContactMedium, Document, Consumer>> getDataBase(Contact contact) {
        Mono<State> state = stateGateway.findStateByName(contact.getState());
        Mono<ContactMedium> medium = contactMediumGateway.findContactMediumByCode(contact.getContactMedium());
        Mono<Document> document = documentGateway.getDocument(contact.getDocumentType());
        Mono<Consumer> consumer = consumerGateway.findConsumerById(contact.getSegment());
        return Mono.zip(state, medium, document, consumer);
    }

    public Mono<StatusResponse<Contact>> updateContact(Contact contact) {
        return stateGateway.findStateByName(contact.getState())
                .switchIfEmpty(Mono.error(new BusinessException(INVALID_DATA)))
                .map(state -> contact.toBuilder().state(Integer.toString(state.getId())).build())
                .flatMap(contactGateway::updateContact)
                .switchIfEmpty(Mono.error(new BusinessException(CONTACT_NOT_FOUND)));
    }

    public Mono<Integer> deleteContact(Contact contact) {
        return contactGateway.findIdContact(contact)
                .switchIfEmpty(Mono.error(new BusinessException(CONTACT_NOT_FOUND)))
                .flatMap(contactGateway::deleteContact);
    }
}
