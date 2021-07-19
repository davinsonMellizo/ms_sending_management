package co.com.bancolombia.contact;

import co.com.bancolombia.AdapterOperations;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.contact.data.ContactData;
import co.com.bancolombia.contact.data.ContactMapper;
import co.com.bancolombia.drivenadapters.TimeFactory;
import co.com.bancolombia.model.client.Client;
import co.com.bancolombia.model.contact.Contact;
import co.com.bancolombia.model.contact.gateways.ContactGateway;
import co.com.bancolombia.model.message.Message;
import co.com.bancolombia.model.response.StatusResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.FIND_ALL_CONTACT_BY_CLIENT_ERROR;

@Repository
public class ContactRepositoryImplement
        extends AdapterOperations<Contact, ContactData, Integer, ContactRepository>
        implements ContactGateway {

    @Autowired
    private TimeFactory timeFactory;

    @Autowired
    public ContactRepositoryImplement(ContactRepository repository, ContactMapper mapper) {
        super(repository, null, mapper::toEntity);
    }

    @Override
    public Flux<Contact> findAllContactsByClient(Message message) {
        return repository.findAllContactsByClient(message.getDocumentNumber(), message.getDocumentType(),
                message.getConsumer())
                .map(Mono::just)
                .flatMap(this::doQuery)
                .map(contact -> contact.toBuilder().id(null).build())
                .onErrorMap(e -> new TechnicalException(e, FIND_ALL_CONTACT_BY_CLIENT_ERROR));
    }
}
