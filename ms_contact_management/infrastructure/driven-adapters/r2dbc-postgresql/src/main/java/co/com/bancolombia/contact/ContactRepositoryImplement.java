package co.com.bancolombia.contact;

import co.com.bancolombia.AdapterOperations;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.contact.data.ContactData;
import co.com.bancolombia.contact.data.ContactMapper;
import co.com.bancolombia.drivenadapters.TimeFactory;
import co.com.bancolombia.model.client.Client;
import co.com.bancolombia.model.contact.Contact;
import co.com.bancolombia.model.contact.gateways.ContactGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.*;

@Repository
public class ContactRepositoryImplement
        extends AdapterOperations<Contact, ContactData, Integer, ContactRepository>
        implements ContactGateway {

    @Autowired
    private TimeFactory timeFactory;

    @Autowired
    public ContactRepositoryImplement(ContactRepository repository, ContactMapper mapper) {
        super(repository, mapper::toData, mapper::toEntity);
    }

    @Override
    public Flux<Contact> contactsByClient(Client client) {
        return repository.findAllContactsByClient(client.getDocumentNumber(), client.getDocumentType())
                .map(Mono::just)
                .flatMap(this::doQuery)
                .onErrorMap(e -> new TechnicalException(e, FIND_ALL_CONTACT_BY_CLIENT_ERROR));
    }
    @Override
    public Flux<Contact> contactsByClientAndSegment(Client client, String segment) {
        return repository.contactsByClientAndSegment(client.getDocumentNumber(), client.getDocumentType(), segment)
                .map(Mono::just)
                .flatMap(this::doQuery)
                .onErrorMap(e -> new TechnicalException(e, FIND_ALL_CONTACT_BY_CLIENT_ERROR));
    }

    @Override
    public Flux<Contact> findIdContact(Contact contact) {
        return repository.findContact(contact.getDocumentNumber(), contact.getDocumentType(),
                contact.getContactMedium(), contact.getSegment())
                .map(this::convertToEntity)
                .onErrorMap(e -> new TechnicalException(e, FIND_CONTACT_ERROR));
    }

    @Override
    public Mono<Contact> saveContact(Contact contact) {
        return save(contact.toBuilder().createdDate(timeFactory.now()).modifiedDate(timeFactory.now()).build())
                .onErrorMap(e -> new TechnicalException(e, SAVE_CONTACT_ERROR));
    }

    @Override
    public Mono<Contact> updateContact(Contact pContact) {
        return Mono.just(pContact)
                .map(this::convertToData)
                .map(contact -> contact.toBuilder().modifiedDate(timeFactory.now()).build())
                .flatMap(repository::save)
                .map(this::convertToEntity)
                .onErrorMap(e -> new TechnicalException(e, UPDATE_CONTACT_ERROR));
    }

    @Override
    public Mono<Contact> deleteContact(Contact contact) {
        return deleteById(contact.getId())
                .onErrorMap(e -> new TechnicalException(e, DELETE_CONTACT_ERROR))
                .thenReturn(contact);
    }

}
