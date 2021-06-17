package co.com.bancolombia.usecase.contact;

import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.client.Client;
import co.com.bancolombia.model.contact.Contact;
import co.com.bancolombia.model.contact.gateways.ContactGateway;
import co.com.bancolombia.model.contactmedium.ContactMedium;
import co.com.bancolombia.model.contactmedium.gateways.ContactMediumGateway;
import co.com.bancolombia.model.enrollmentcontact.EnrollmentContact;
import co.com.bancolombia.model.enrollmentcontact.gateways.EnrollmentContactGateway;
import co.com.bancolombia.model.response.ContactsResponse;
import co.com.bancolombia.model.response.StatusResponse;
import co.com.bancolombia.model.state.State;
import co.com.bancolombia.model.state.gateways.StateGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple3;

import static co.com.bancolombia.commons.enums.BusinessErrorMessage.CONTACT_NOT_FOUND;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.INVALID_DATA;

@RequiredArgsConstructor
public class ContactUseCase {
    private final StateGateway stateGateway;
    private final ContactGateway contactGateway;
    private final ContactMediumGateway contactMediumGateway;
    private final EnrollmentContactGateway enrollmentContactGateway;

    public Mono<ContactsResponse> findContactsByClient(Client client) {
        return contactGateway.findAllContactsByClient(client)
                .collectList()
                .map(contacts -> ContactsResponse.builder().contacts(contacts).build());
    }

    public Mono<Contact> saveContact(Contact contact) {
        return Mono.just(contact)
                .flatMap(this::getDataBase)
                .switchIfEmpty(Mono.error(new BusinessException(INVALID_DATA)))
                .map(data -> contact.toBuilder()
                        .idState(data.getT1().getId())
                        .idEnrollmentContact(data.getT2().getId())
                        .idContactMedium(data.getT3().getId())
                        .build())
                .flatMap(contactGateway::saveContact);
    }

    private Mono<Tuple3<State, EnrollmentContact, ContactMedium>> getDataBase(Contact contact) {
        Mono<State> state = stateGateway.findStateByName(contact.getState());
        Mono<EnrollmentContact> enrollment = enrollmentContactGateway
                .findEnrollmentContactByCode(contact.getEnrollmentContact());
        Mono<ContactMedium> medium = contactMediumGateway.findContactMediumByCode(contact.getContactMedium());
        return Mono.zip(state, enrollment, medium);
    }

    public Mono<StatusResponse<Contact>> updateContact(Contact contact) {
        return stateGateway.findStateByName(contact.getState())
                .switchIfEmpty(Mono.error(new BusinessException(INVALID_DATA)))
                .map(state -> contact.toBuilder().idState(state.getId()).build())
                .flatMap(contactGateway::updateContact)
                .switchIfEmpty(Mono.error(new BusinessException(CONTACT_NOT_FOUND)));
    }

    public Mono<Integer> deleteContact(Contact contact) {
        return contactGateway.findIdContact(contact)
                .switchIfEmpty(Mono.error(new BusinessException(CONTACT_NOT_FOUND)))
                .flatMap(contactGateway::deleteContact);
    }
}
