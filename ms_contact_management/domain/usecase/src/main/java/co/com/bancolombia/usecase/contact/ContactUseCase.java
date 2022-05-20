package co.com.bancolombia.usecase.contact;

import co.com.bancolombia.commons.enums.DocumentTypeEnum;
import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.client.Client;
import co.com.bancolombia.model.client.Enrol;
import co.com.bancolombia.model.client.gateways.ClientGateway;
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
import co.com.bancolombia.model.response.StatusResponse;
import co.com.bancolombia.model.state.State;
import co.com.bancolombia.model.state.gateways.StateGateway;
import co.com.bancolombia.usecase.log.NewnessUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple4;

import java.util.List;
import java.util.regex.Pattern;

import static co.com.bancolombia.commons.constants.ContactWay.MAIL;
import static co.com.bancolombia.commons.constants.ContactWay.SMS;
import static co.com.bancolombia.commons.constants.Transaction.*;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.*;
import static co.com.bancolombia.commons.enums.State.ACTIVE;
import static co.com.bancolombia.commons.enums.State.INACTIVE;

@RequiredArgsConstructor
public class ContactUseCase {
    private final StateGateway stateGateway;
    private final ContactGateway contactGateway;
    private final NewnessUseCase newnessUseCase;
    private final DocumentGateway documentGateway;
    private final ConsumerGateway consumerGateway;
    private final ContactMediumGateway contactMediumGateway;
    private final ClientRepository clientRepository;
    private final ClientGateway clientGateway;

    public Mono<ResponseContacts> findContactsByClient(Client pClient, String consumerCode) {
        return clientRepository.findClientByIdentification(pClient)
                .switchIfEmpty(Mono.error(new BusinessException(CLIENT_NOT_FOUND)))
                .filter(client -> client.getIdState() == ACTIVE.getType())
                .flatMap(client -> findAllContacts(client, consumerCode))
                .switchIfEmpty(Mono.error(new BusinessException(CLIENT_INACTIVE)))
                .onErrorResume(e -> e.getMessage().equals(CLIENT_NOT_FOUND.getMessage()),
                        e-> clientGateway.retrieveAlertInformation(pClient))
                .switchIfEmpty(Mono.error(new BusinessException(CLIENT_NOT_FOUND)));
    }

    private Mono<ResponseContacts> findAllContacts(Client client, String consumerCode) {
        return Mono.just(consumerCode)
                .filter(consumerFilter -> !consumerFilter.isEmpty())
                .flatMap(consumerGateway::findConsumerById)
                .flatMapMany(consumer -> contactGateway.contactsByClientAndSegment(client, consumer.getSegment()))
                .switchIfEmpty(contactGateway.contactsByClient(client))
                .collectList()
                .map(contacts -> ResponseContacts.<Contact>builder()
                        .contacts(contacts)
                        .documentNumber(client.getDocumentNumber())
                        .documentType( String.valueOf(DocumentTypeEnum
                                .fromId(client.getDocumentType()).getValue()))
                        .enrollmentOrigin(client.getEnrollmentOrigin())
                        .keyMdm(client.getKeyMdm())
                        .status(client.getIdState() == ACTIVE.getType() ? ACTIVE.getValue(): INACTIVE.getValue())
                        .creationUser(client.getCreationUser())
                        .createdDate(client.getCreatedDate())
                        .modifiedDate(client.getModifiedDate())
                        .build());
    }

    public Mono<Contact> saveContact(Contact pContact, String voucher) {
        return Mono.just(pContact)
                .flatMap(this::getDataBase)
                .switchIfEmpty(Mono.error(new BusinessException(INVALID_DATA)))
                .map(data -> pContact.toBuilder()
                        .stateContact(Integer.toString(data.getT1().getId()))
                        .contactWay(Integer.toString(data.getT2().getId()))
                        .documentType(data.getT3().getId())
                        .previous(false)
                        .segment(data.getT4().getSegment())
                        .build())
                .flatMap(contactGateway::saveContact)
                .flatMap(contact -> newnessUseCase.saveNewness(contact, CREATE_CONTACT, voucher));
    }

    private Mono<Tuple4<State, ContactMedium, Document, Consumer>> getDataBase(Contact contact) {
        Mono<State> state = stateGateway.findState(contact.getStateContact());
        Mono<ContactMedium> medium = contactMediumGateway.findContactMediumByCode(contact.getContactWay());
        Mono<Document> document = documentGateway.getDocument(contact.getDocumentType());
        Mono<Consumer> consumer = consumerGateway.findConsumerById(contact.getSegment());
        return Mono.zip(state, medium, document, consumer);
    }

    public Mono<StatusResponse<Contact>> updateContactRequest(Contact newContact, String voucher) {
        return clientRepository.findClientByIdentification(Client.builder().documentType(newContact.getDocumentType())
                        .documentNumber(newContact.getDocumentNumber()).build())
                .switchIfEmpty(Mono.error(new BusinessException(CLIENT_NOT_FOUND)))
                .flatMap(client -> consumerGateway.findConsumerById(newContact.getSegment()))
                .map(consumer -> newContact.toBuilder().segment(consumer.getSegment()).build())
                .flatMapMany(contactGateway::findIdContact)
                .switchIfEmpty(Mono.error(new BusinessException(CONTACT_NOT_FOUND)))
                .collectList()
                .flatMap(contacts -> validateNewValueContact(contacts, newContact, voucher)
                        .switchIfEmpty(updateValueContact(contacts, newContact, voucher)));
    }

    private Mono<StatusResponse<Contact>> validateNewValueContact(List<Contact> contacts,
                                                                  Contact newContact, String voucher) {
        return Flux.fromIterable(contacts)
                .filter(contact -> !contact.getPrevious())
                .next()
                .filter(contact -> contact.getValue().equals(newContact.getValue()))
                .flatMap(contact -> updateStateContact(contact, newContact.getStateContact()))
                .flatMap(contactGateway::updateContact)
                .zipWith(Flux.fromIterable(contacts).filter(contact -> !contact.getPrevious()).next())
                .map(response -> StatusResponse.<Contact>builder()
                        .before(response.getT2()).actual(response.getT1()).build())
                .flatMap(response -> newnessUseCase.saveNewness(response.getBefore(), UPDATE_CONTACT, voucher)
                        .thenReturn(response));

    }

    private Mono<Contact> updateStateContact(Contact contact, String nameState) {
        return stateGateway.findState(nameState)
                .switchIfEmpty(Mono.error(new BusinessException(STATE_INVALID)))
                .map(state -> contact.toBuilder().stateContact(Integer.toString(state.getId())).build());
    }

    public Mono<StatusResponse<Contact>> updateValueContact(List<Contact> contacts,
                                                            Contact newContact, String voucher) {
        return Mono.just(newContact)
                .map(contact -> contacts)
                .flatMapMany(Flux::fromIterable)
                .filter(Contact::getPrevious)
                .flatMap(contact -> deletePrevious(contact, contacts, voucher))
                .switchIfEmpty(Flux.fromIterable(contacts)).next()
                .map(contact -> contact.toBuilder().previous(true).build())
                .flatMap(contactGateway::updateContact)
                .zipWhen(contact -> saveCopyPrevious(contact, newContact, voucher))
                .map(response -> StatusResponse.<Contact>builder()
                        .before(response.getT1()).actual(response.getT2()).build());
    }

    private Flux<Contact> deletePrevious(Contact pContact, List<Contact> contacts, String voucher) {
        return contactGateway.deleteContact(pContact)
                .flatMap(contact -> newnessUseCase.saveNewness(contact, DELETE_CONTACT_PREVIOUS, voucher))
                .map(idContact -> contacts)
                .flatMapMany(Flux::fromIterable)
                .filter(contact1 -> !contact1.getPrevious());
    }

    private Mono<Contact> saveCopyPrevious(Contact pContact, Contact newContact, String voucher) {
        return stateGateway.findState(newContact.getStateContact())
                .onErrorMap(e -> new BusinessException(INVALID_DATA))
                .map(state -> newContact.toBuilder().stateContact(Integer.toString(state.getId()))
                        .contactWay(pContact.getContactWay())
                        .documentType(pContact.getDocumentType())
                        .segment(pContact.getSegment())
                        .previous(false)
                        .build())
                .flatMap(contactGateway::saveContact)
                .flatMap(contact -> newnessUseCase.saveNewness(contact, UPDATE_CONTACT, voucher));
    }

    public Mono<Client> validatePhone(Enrol enrol, Client client) {
        return Flux.fromIterable(enrol.getContactData())
                .filter(cnt -> SMS.equals(cnt.getContactWay()) )
                .filter(cnt -> !(cnt.getValue().chars().allMatch(Character::isDigit)
                        && cnt.getValue().length() >= 10))
                .next()
                .flatMap(contact -> Mono.error(new BusinessException(INVALID_PHONE)))
                .map(contact -> client)
                .switchIfEmpty(Mono.just(client));
    }

    public Mono<Client> validateMail(Enrol enrol, Client client) {
        final var pattern = "^(([0-9a-zA-Z]+[-._+&])*[0-9a-zA-Z]+)+@([-0-9a-zA-Z]+[.])+[a-zA-Z]{2,6}$";
        return Flux.fromIterable(enrol.getContactData())
                .filter(cnt -> MAIL.equals(cnt.getContactWay()))
                .filter(cnt -> !Pattern.compile(pattern).matcher(cnt.getValue()).matches())
                .next()
                .flatMap(contact -> Mono.error(new BusinessException(INVALID_EMAIL)))
                .map(o -> client)
                .switchIfEmpty(Mono.just(client));
    }

    public Mono<Client> validateContacts(Enrol enrol) {
        return Mono.just(enrol)
                .filter(enrol1 -> enrol1.getContactData() != null && !enrol1.getContactData().isEmpty())
                .map(Enrol::getClient)
                .switchIfEmpty(Mono.error(new BusinessException(CONTACTS_EMPTY)));
    }
}
