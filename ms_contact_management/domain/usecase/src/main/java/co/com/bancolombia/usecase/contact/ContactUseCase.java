package co.com.bancolombia.usecase.contact;

import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.client.Client;
import co.com.bancolombia.model.contact.Contact;
import co.com.bancolombia.model.contact.gateways.ContactGateway;
import co.com.bancolombia.model.response.ContactsResponse;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.List;

import static co.com.bancolombia.commons.enums.BusinessErrorMessage.CONTACT_NOT_FOUND;

@RequiredArgsConstructor
public class ContactUseCase {
    private final ContactGateway contactGateway;

    public Mono<ContactsResponse> findContactsByClient(Client client){
        return contactGateway.findAllContactsByClient(client)
                .collectList()
                .map(contacts -> ContactsResponse.builder().contacts(contacts).build());
    }

    public Mono<Contact> saveContact(Contact contact){
        return contactGateway.saveContact(contact.toBuilder()
                .idContactMedium(0)
                .idEnrollmentContact(0)
                .idState(0)
                .build());
    }

    public Mono<ContactsResponse> updateContact(Contact contact){
        return contactGateway.updateContact(contact)
                .switchIfEmpty(Mono.error(new BusinessException(CONTACT_NOT_FOUND)))
                .map(contactUpdate -> List.of(contactUpdate, contact))
                .map(contactUpdated -> ContactsResponse.builder().contacts(contactUpdated).build());
    }

    public Mono<Integer> deleteContact(Contact contact){
        return contactGateway.findIdContact(contact)
                .switchIfEmpty(Mono.error(new BusinessException(CONTACT_NOT_FOUND)))
                .flatMap(integer -> contactGateway.deleteContact(integer));
    }
}
