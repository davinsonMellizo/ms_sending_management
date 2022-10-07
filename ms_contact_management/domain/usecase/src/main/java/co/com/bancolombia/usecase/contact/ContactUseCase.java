package co.com.bancolombia.usecase.contact;

import co.com.bancolombia.commons.enums.BusinessErrorMessage;
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
import co.com.bancolombia.model.document.gateways.DocumentGateway;
import co.com.bancolombia.model.response.StatusResponse;
import co.com.bancolombia.model.state.State;
import co.com.bancolombia.model.state.gateways.StateGateway;
import co.com.bancolombia.usecase.log.NewnessUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.regex.Pattern;

import static co.com.bancolombia.commons.constants.ContactWay.MAIL;
import static co.com.bancolombia.commons.constants.ContactWay.SMS;
import static co.com.bancolombia.commons.constants.Transaction.CREATE_CONTACT;
import static co.com.bancolombia.commons.constants.Transaction.DELETE_CONTACT_PREVIOUS;
import static co.com.bancolombia.commons.constants.Transaction.UPDATE_CONTACT;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.CLIENT_INACTIVE;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.CLIENT_NOT_FOUND;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.CONTACTS_EMPTY;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.INVALID_DATA;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.INVALID_EMAIL;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.INVALID_ENVIRONMENT;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.INVALID_PHONE;
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
                .switchIfEmpty(Mono.error(new BusinessException(CLIENT_INACTIVE)))
                .map(client -> client.toBuilder().documentType(pClient.getDocumentType()).build())
                .flatMap(client -> findAllContacts(client, consumerCode))
                .onErrorResume(e -> e.getMessage().equals(CLIENT_NOT_FOUND.getMessage()),
                        e-> documentGateway.getDocument(pClient.getDocumentType())
                                .flatMap(document -> clientGateway.retrieveAlertInformation(pClient.toBuilder()
                                .documentType(document.getCode())
                                .build())))
                .switchIfEmpty(Mono.error(new BusinessException(CLIENT_NOT_FOUND)));
    }

    private Mono<ResponseContacts> findAllContacts(Client client, String consumerCode) {
        return Mono.just(consumerCode)
                .filter(consumerFilter -> !consumerFilter.isEmpty())
                .flatMap(this::findConsumer)
                .flatMap(consumer -> contactGateway.contactsByClientAndSegment(client, consumer.getSegment()))
                .switchIfEmpty(contactGateway.contactsByClient(client))
                .map(contacts -> ResponseContacts.<Contact>builder()
                        .contacts(contacts)
                        .documentNumber(client.getDocumentNumber())
                        .documentType(client.getDocumentType())
                        .enrollmentOrigin(client.getEnrollmentOrigin())
                        .delegate(client.getDelegate())
                        .preference(client.getPreference())
                        .keyMdm(client.getKeyMdm())
                        .status(client.getIdState() == ACTIVE.getType() ? ACTIVE.getValue(): INACTIVE.getValue())
                        .creationUser(client.getCreationUser())
                        .createdDate(client.getCreatedDate())
                        .modifiedDate(client.getModifiedDate())
                        .build());
    }
    private Mono<Consumer> findConsumer(String consumerCode){
        return Mono.just(consumerCode)
                .flatMap(consumerGateway::findConsumerById)
                .switchIfEmpty(Mono.error(new BusinessException(CLIENT_INACTIVE)));
    }

    public Mono<Contact> saveContact(Contact pContact, String voucher) {
        return Mono.just(pContact)
                .flatMap(contactGateway::saveContact)
                .flatMap(contact -> newnessUseCase.saveNewness(contact, CREATE_CONTACT, voucher));
    }

    public Mono<StatusResponse<Contact>> updateContactRequest(Contact newContact, String voucher) {
        return clientRepository.findClientByIdentification(Client.builder().documentType(newContact.getDocumentType())
                        .documentNumber(newContact.getDocumentNumber()).build())
                .flatMap(client -> consumerGateway.findConsumerById(newContact.getSegment()))
                .map(consumer -> newContact.toBuilder().segment(consumer.getSegment()).build())
                .flatMapMany(contactGateway::findContactsByClientSegmentAndMedium)
                .collectList()
                .filter(contacts -> !contacts.isEmpty())
                .flatMap(contacts -> updateValueContact(contacts, newContact, voucher))
                .switchIfEmpty(getDataBase(newContact)
                        .flatMap(contact -> saveContact(contact, voucher))
                        .map(contact -> StatusResponse.<Contact>builder()
                        .actual(contact).before(contact)
                        .build()));
    }

    public Mono<StatusResponse<Contact>> updateValueContact(List<Contact> contacts,
                                                            Contact newContact, String voucher) {
        return Flux.fromIterable(contacts)
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

    public Mono<Enrol> validatePhone(Enrol enrol) {
        return Flux.fromIterable(enrol.getContactData())
                .filter(cnt -> SMS.equals(cnt.getContactWay()) )
                .filter(cnt -> !(cnt.getValue().chars().allMatch(Character::isDigit)
                        && cnt.getValue().length() >= 10))
                .next()
                .flatMap(contact -> Mono.error(new BusinessException(INVALID_PHONE)))
                .map(contact -> enrol)
                .switchIfEmpty(Mono.just(enrol));
    }

    public Mono<Enrol> validateMail(Enrol enrol) {
        final var pattern = "^(([0-9a-zA-Z]+[-._+&])*[0-9a-zA-Z]+)+@([-0-9a-zA-Z]+[.])+[a-zA-Z]{2,6}$";
        return Flux.fromIterable(enrol.getContactData())
                .filter(cnt -> MAIL.equals(cnt.getContactWay()))
                .flatMap(this::validateEnvironment)
                .filter(cnt -> !Pattern.compile(pattern).matcher(cnt.getValue()).matches())
                .next()
                .flatMap(contact -> Mono.error(new BusinessException(INVALID_EMAIL)))
                .map(o -> enrol)
                .switchIfEmpty(Mono.just(enrol));
    }

    private Mono<Contact> validateEnvironment(Contact contact) {
        return Mono.just(contact)
                .filter(cnt -> !cnt.getEnvironmentType().isEmpty())
                .switchIfEmpty(Mono.error(new BusinessException(INVALID_ENVIRONMENT)));
    }

    public Mono<Enrol> validateContacts(Enrol enrol) {
        return Mono.just(enrol)
                .filter(enrol1 -> enrol1.getContactData() != null && !enrol1.getContactData().isEmpty())
                .switchIfEmpty(Mono.error(new BusinessException(CONTACTS_EMPTY)))
                .flatMapMany(enrol1 ->Flux.fromIterable(enrol.getContactData()))
                .map(contact -> contact.toBuilder().documentType(enrol.getClient().getDocumentType())
                        .documentNumber(enrol.getClient().getDocumentNumber())
                        .segment(enrol.getClient().getConsumerCode()).build())
                .flatMap(this::getDataBase)
                .collectList()
                .doOnNext(enrol::setContactData)
                .thenReturn(enrol);
    }
    private Mono<Contact> getDataBase(Contact contact) {
        Mono<State> state = stateGateway.findState(contact.getStateContact());
        Mono<ContactMedium> medium = contactMediumGateway.findContactMediumByCode(contact.getContactWay());
        Mono<Consumer> consumer = consumerGateway.findConsumerById(contact.getSegment());
        return Mono.zip(state, medium, consumer)
                .switchIfEmpty(Mono.error(new BusinessException(INVALID_DATA)))
                .map(data -> contact.toBuilder()
                        .stateContact(Integer.toString(data.getT1().getId()))
                        .contactWay(Integer.toString(data.getT2().getId()))
                        .contactWayName(data.getT2().getCode())
                        .previous(false)
                        .segment(data.getT3().getSegment())
                        .build())
                ;
    }
}
