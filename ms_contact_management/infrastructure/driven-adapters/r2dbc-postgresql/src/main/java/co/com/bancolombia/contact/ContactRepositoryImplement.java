package co.com.bancolombia.contact;

import co.com.bancolombia.AdapterOperations;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.contact.data.ContactData;
import co.com.bancolombia.contact.data.ContactMapper;
import co.com.bancolombia.contact.reader.ContactRepositoryReader;
import co.com.bancolombia.contact.writer.ContactRepository;
import co.com.bancolombia.drivenadapters.TimeFactory;
import co.com.bancolombia.model.client.Client;
import co.com.bancolombia.model.contact.Contact;
import co.com.bancolombia.model.contact.gateways.ContactGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.DELETE_CONTACT_ERROR;
import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.FIND_ALL_CONTACT_BY_CLIENT_ERROR;
import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.FIND_CONTACT_ERROR;
import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.SAVE_CONTACT_ERROR;
import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.UPDATE_CONTACT_ERROR;

@Repository
public class ContactRepositoryImplement
        extends AdapterOperations<Contact, ContactData, Integer, ContactRepository, ContactRepositoryReader>
        implements ContactGateway {

    @Autowired
    private TimeFactory timeFactory;

    @Autowired
    public ContactRepositoryImplement(ContactRepository repository, ContactRepositoryReader repositoryReader,
                                      ContactMapper mapper) {
        super(repository, repositoryReader, mapper::toData, mapper::toEntity);
    }

    @Override
    public Mono<List<Contact>> contactsByClient(Client client) {
        return repositoryRead.findAllContactsByClient(client.getDocumentNumber(), client.getDocumentType())
                .map(Mono::just)
                .flatMap(this::doQuery)
                .collectList()
                .onErrorMap(e -> new TechnicalException(e, FIND_ALL_CONTACT_BY_CLIENT_ERROR));
    }
    @Override
    public Mono<List<Contact>> contactsByClientAndSegment(Client client, String segment) {
        return repositoryRead.contactsByClientAndSegment(client.getDocumentNumber(), client.getDocumentType(), segment)
                .map(Mono::just)
                .flatMap(this::doQuery)
                .collectList()
                .onErrorMap(e -> new TechnicalException(e, FIND_ALL_CONTACT_BY_CLIENT_ERROR));
    }

    @Override
    public Flux<Contact> findContactsByClientSegmentAndMedium(Contact contact) {
        return repositoryRead.findContact(contact.getDocumentNumber(), contact.getDocumentType(),
                contact.getContactWay(), contact.getSegment())
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
                .flatMap(this::saveData)
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
