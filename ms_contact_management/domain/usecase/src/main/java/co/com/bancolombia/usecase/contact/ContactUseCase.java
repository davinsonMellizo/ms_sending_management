package co.com.bancolombia.usecase.contact;

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
import static co.com.bancolombia.commons.constants.Transaction.UPDATE_CONTACT;
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
                .filter(client -> client.getIdState().equals(ACTIVE.getType()))
                .switchIfEmpty(Mono.error(new BusinessException(CLIENT_INACTIVE)))
                .map(client -> client.toBuilder().documentType(pClient.getDocumentType()).build())
                .flatMap(client -> findContactsCloud(client, consumerCode))
                .onErrorResume(e -> e.getMessage().equals(CLIENT_NOT_FOUND.getMessage()),
                        e-> findClientsIseries(pClient,consumerCode));
    }

    private Mono<ResponseContacts> findContactsCloud(Client pClient, String pConsumerCode){
        return Mono.just(pConsumerCode)
                .filter(consumerCode -> !consumerCode.isEmpty())
                .flatMap(this::findConsumer)
                .flatMap(consumer -> findClientByChanelCloud(pClient, consumer.getSegment()))
                .switchIfEmpty(findClientWithoutChanelCloud(pClient));
    }

    private Mono<ResponseContacts> findClientsIseries(Client pClient, String pConsumerCode){
        return Mono.just(pConsumerCode)
                .filter(consumerCode -> !consumerCode.isEmpty())
                .flatMap(this::findConsumer)
                .flatMap(consumer -> findClientByChanelIseries(pClient, consumer.getSegment()))
                .switchIfEmpty(findClientWithoutChanelIseries(pClient))
                .onErrorMap(e-> e.getMessage().equals(CLIENT_NOT_FOUND_PER_CHANNEL.getMessage()),
                        e-> new BusinessException(CLIENT_NOT_FOUND))
                .switchIfEmpty(Mono.error(new BusinessException(CLIENT_NOT_FOUND)));
    }

    private Mono<ResponseContacts> findClientByChanelCloud(Client client, String segment){
        return contactGateway.contactsByClientAndSegment(client, segment)
                .filter(contacts -> !contacts.isEmpty())
                .flatMap(contacts -> buildResponse(client,contacts))
                .switchIfEmpty(findClientByChanelIseries(client,segment))
                .switchIfEmpty(Mono.error(new BusinessException(CLIENT_NOT_FOUND_PER_CHANNEL)));

    }
    private Mono<ResponseContacts> findClientByChanelIseries(Client client, String segment){
        return documentGateway.getDocument(client.getDocumentType())
                .switchIfEmpty(Mono.error(new BusinessException(DOCUMENT_TYPE_NOT_FOUND)))
                .flatMap(document ->clientGateway.retrieveAlertInformation(client.toBuilder()
                        .documentType(document.getCode()).build()))
                .flatMap(contacts -> filterContactsByConsumer(contacts, segment));
    }

    private Mono<ResponseContacts> findClientWithoutChanelCloud(Client client){
        return contactGateway.contactsByClient(client)
                .flatMap(contacts -> buildResponse(client,contacts));
    }
    private Mono<ResponseContacts> findClientWithoutChanelIseries(Client client){
        return documentGateway.getDocument(client.getDocumentType())
                .switchIfEmpty(Mono.error(new BusinessException(DOCUMENT_TYPE_NOT_FOUND)))
                .flatMap(document ->clientGateway.retrieveAlertInformation(client.toBuilder()
                                .documentType(document.getCode())
                                .build()));
    }

    private Mono<ResponseContacts> buildResponse(Client client, List<Contact> contacts){
        return Mono.just(
                ResponseContacts.<Contact>builder()
                        .contacts(contacts)
                        .documentNumber(client.getDocumentNumber())
                        .documentType(client.getDocumentType())
                        .enrollmentOrigin(client.getEnrollmentOrigin())
                        .delegate(client.getDelegate())
                        .preference(client.getPreference())
                        .keyMdm(client.getKeyMdm())
                        .status(client.getIdState().equals( ACTIVE.getType()) ? ACTIVE.getValue(): INACTIVE.getValue())
                        .creationUser(client.getCreationUser())
                        .createdDate(client.getCreatedDate())
                        .modifiedDate(client.getModifiedDate())
                        .build()
        );
    }

    private Mono<ResponseContacts> filterContactsByConsumer(ResponseContacts responseContacts, String segment){
        return  Mono.just(responseContacts.getContacts())
                .filter(contacts -> !contacts.isEmpty())
                .flatMapMany(Flux::fromIterable)
                .filter(contact -> contact.getSegment().equals(segment))
                .collectList()
                .filter(contacts -> !contacts.isEmpty())
                .map(contacts -> responseContacts.toBuilder().contacts(contacts).build())
                .switchIfEmpty(Mono.error(new BusinessException(CLIENT_NOT_FOUND_PER_CHANNEL)));
    }


    private Mono<Consumer> findConsumer(String consumerCode){
        return Mono.just(consumerCode)
                .flatMap(consumerGateway::findConsumerById)
                .switchIfEmpty(Mono.error(new BusinessException(CONSUMER_NOT_FOUND)));
    }

    public Mono<Contact> saveContact(Contact pContact, String voucher) {
        return Mono.just(pContact)
                .flatMap(this::validateCountryCode)
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
                .filter(contact -> !contact.getPrevious()).next()
                .map(current -> StatusResponse.<Contact>builder().actual(current).build())
                .flatMap(response -> updateCurrentContact(response, newContact, voucher))
                .flatMap(response -> updatePrevious(response, contacts, voucher));
    }

    private Mono<StatusResponse<Contact>> updateCurrentContact(StatusResponse<Contact> response, Contact newContact,
                                                               String voucher) {
        return stateGateway.findState(newContact.getStateContact())
                .switchIfEmpty(Mono.error(new BusinessException(INVALID_DATA)))
                .zipWith(validateCountryCode(newContact))
                .map(data -> response.getActual().toBuilder().value(data.getT2().getValue())
                        .stateContact(data.getT1().getId().toString())
                        .environmentType(data.getT2().getEnvironmentType())
                        .build())
                .flatMap(contactGateway::updateContact)
                .doOnNext(contactResponse -> newnessUseCase.saveNewness(response.getActual(), UPDATE_CONTACT, voucher))
                .map(contact -> response.toBuilder().before(response.getActual()).actual(contact).build());

    }

    private Mono<StatusResponse<Contact>> updatePrevious(StatusResponse<Contact> response, List<Contact> contacts,
                                                         String voucher) {
        return Flux.fromIterable(contacts)
                .filter(Contact::getPrevious).next()
                .doOnNext(previous -> newnessUseCase.saveNewness(previous, UPDATE_CONTACT, voucher))
                .map(contactPrevious -> contactPrevious.toBuilder().value(response.getBefore().getValue())
                        .idState(response.getBefore().getIdState())
                        .environmentType(response.getBefore().getEnvironmentType())
                        .build())
                .flatMap(contactGateway::updateContact)
                .switchIfEmpty(contactGateway.saveContact(contacts.get(0).toBuilder().id(null).previous(true).build()))
                .thenReturn(response);
    }

    public Mono<Enrol> validatePhone(Enrol enrol) {
        return Flux.fromIterable(enrol.getContactData())
                .filter(cnt -> SMS.equals(cnt.getContactWay()) )
                .filter(cnt -> cnt.getValue().length() < 10)
                .next()
                .flatMap(contact -> Mono.error(new BusinessException(INVALID_PHONE)))
                .map(contact -> enrol)
                .switchIfEmpty(Mono.just(enrol));
    }

    private Mono<Contact> validateCountryCode(Contact contact){
        return Mono.just(contact)
                .filter(contact1 -> contact1.getContactWayName().equals(SMS))
                .filter(contact1 -> !"+".equals(contact1.getValue().substring(0,1)))
                .map(contact1 -> contact1.toBuilder().value("+57"+contact.getValue()).build())
                .switchIfEmpty(Mono.just(contact));
    }

    public Mono<Enrol> validateMail(Enrol enrol) {
        final var pattern = "^\\w++([-._+&]\\w++)*+@\\w++([.]\\w++)++$";
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
