package co.com.bancolombia.usecase.contact;

import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.client.Client;
import co.com.bancolombia.model.client.gateways.ClientRepository;
import co.com.bancolombia.model.consumer.Consumer;
import co.com.bancolombia.model.consumer.gateways.ConsumerGateway;
import co.com.bancolombia.model.contact.Contact;
import co.com.bancolombia.model.contact.ResponseContacts;
import co.com.bancolombia.model.contact.gateways.ContactGateway;
import co.com.bancolombia.model.contactmedium.ContactMedium;
import co.com.bancolombia.model.contactmedium.gateways.ContactMediumGateway;
import co.com.bancolombia.model.document.Document;
import co.com.bancolombia.model.document.gateways.DocumentGateway;
import co.com.bancolombia.model.message.gateways.MessageGateway;
import co.com.bancolombia.model.response.StatusResponse;
import co.com.bancolombia.model.state.State;
import co.com.bancolombia.model.state.gateways.StateGateway;
import co.com.bancolombia.usecase.commons.ValidateData;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple4;


import java.util.List;

import static co.com.bancolombia.commons.constants.State.ACTIVE;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.*;
import static co.com.bancolombia.usecase.commons.ValidateData.isValidMailOrMobile;

@RequiredArgsConstructor
public class ContactUseCase {
    private final StateGateway stateGateway;
    private final ContactGateway contactGateway;
    private final MessageGateway messageGateway;
    private final DocumentGateway documentGateway;
    private final ConsumerGateway consumerGateway;
    private final ContactMediumGateway contactMediumGateway;
    private final ClientRepository clientRepository;

    public Mono<ResponseContacts> findContactsByClient(Client pClient) {
        return clientRepository.findClientByIdentification(pClient)
                .switchIfEmpty(Mono.error(new BusinessException(CLIENT_NOT_FOUND)))
                .filter(client -> client.getIdState()==ACTIVE)
                .flatMap(this::findAllContacts)
                .switchIfEmpty(Mono.error(new BusinessException(CLIENT_INACTIVE)));
    }

    private Mono<ResponseContacts> findAllContacts(Client client) {
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
                        .documentType(data.getT3().getId()).previous(false)
                        .segment(data.getT4().getSegment())
                        .build())
                .flatMap(contactGateway::saveContact);
    }

    private Mono<Tuple4<State, ContactMedium, Document, Consumer>> getDataBase(Contact contact) {
        Mono<State> state = stateGateway.findState(contact.getState());
        Mono<ContactMedium> medium = contactMediumGateway.findContactMediumByCode(contact.getContactMedium());
        Mono<Document> document = documentGateway.getDocument(contact.getDocumentType());
        Mono<Consumer> consumer = consumerGateway.findConsumerById(contact.getSegment());
        return Mono.zip(state, medium, document, consumer);
    }

    public Mono<StatusResponse<Contact>> updateContactRequest(Contact contact){
        return clientRepository.findClientByIdentification(Client.builder().documentType(contact.getDocumentType())
                .documentNumber(contact.getDocumentNumber())
                .build())
                .doOnNext(client -> contact.setDocumentType(client.getDocumentType()))
                .switchIfEmpty(Mono.error(new BusinessException(CLIENT_NOT_FOUND)))
                .flatMapMany(client -> contactGateway.findIdContact(contact))
                .switchIfEmpty(Mono.error(new BusinessException(CONTACT_NOT_FOUND)))
                .collectList()
                .doOnNext(System.out::println)
                .flatMap(contacts -> validateNewValueContact(contacts, contact)
                        .switchIfEmpty(updateValueContact(contacts, contact)));
    }

    private Mono<StatusResponse<Contact>> validateNewValueContact(List<Contact> contacts, Contact newContact) {
        return Flux.fromIterable(contacts)
                .filter(contact -> !contact.getPrevious())
                .next()
                .filter(contact -> contact.getValue().equals(newContact.getValue()))
                .flatMap(contact -> updateStateContact(contact, newContact.getState()))
                .flatMap(contactGateway::updateContact)
                .zipWith(Flux.fromIterable(contacts).filter(contact -> !contact.getPrevious()).next())
                .map(response -> StatusResponse.<Contact>builder()
                        .description("Contact Updated Successfully")
                        .before(response.getT2()).actual(response.getT1()).build());
    }

    private Mono<Contact> updateStateContact(Contact contact, String nameState){
        return stateGateway.findState(nameState)
                .switchIfEmpty(Mono.error(new BusinessException(STATE_INVALID)))
                .map(state -> contact.toBuilder().state(Integer.toString(state.getId())).build());
    }

    public Mono<StatusResponse<Contact>> updateValueContact(List<Contact> contacts, Contact newContact) {
        return Mono.just(newContact)
                .filter(isValidMailOrMobile)
                .map(contact -> contacts)
                .switchIfEmpty(Mono.error(new BusinessException(CONTACT_INVALID)))
                .flatMapMany(Flux::fromIterable)
                .filter(contact -> contact.getPrevious())
                .flatMap(contact -> deletePrevious(contact, contacts))
                .switchIfEmpty(Flux.fromIterable(contacts)).next()
                .map(contact -> contact.toBuilder().previous(true).build())
                .flatMap(contactGateway::updateContact)
                .zipWith(saveCopyPrevious(newContact))
                .map(response -> StatusResponse.<Contact>builder()
                        .description("Contact Updated Successfully")
                        .before(response.getT1()).actual(response.getT2()).build());
    }

    private Flux<Contact> deletePrevious(Contact contact, List<Contact> contacts){
        return contactGateway.deleteContact(contact.getId())
                .map(idContact -> contacts)
                .flatMapMany(Flux::fromIterable)
                .filter(contact1 -> !contact1.getPrevious());
    }

    private Mono<Contact> saveCopyPrevious(Contact newContact){
        return stateGateway.findState(newContact.getState())
                .zipWith(contactMediumGateway.findContactMediumByCode(newContact.getContactMedium()))
                .onErrorMap(e -> new BusinessException(INVALID_DATA))
                .map(data -> newContact.toBuilder().state(Integer.toString(data.getT1().getId()))
                        .contactMedium(Integer.toString(data.getT2().getId())).previous(false)
                        .build())
                .flatMap(contactGateway::saveContact);

    }

    public Mono<Integer> deleteContact(Contact contact) {
        return contactGateway.findIdContact(contact)
                .map(Contact::getId)
                .switchIfEmpty(Mono.error(new BusinessException(CONTACT_NOT_FOUND)))
                .flatMap(contactGateway::deleteContact)
                .last();
    }
}
