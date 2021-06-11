package co.com.bancolombia.client;

import co.com.bancolombia.AdapterOperations;
import co.com.bancolombia.client.data.ClientData;
import co.com.bancolombia.client.data.ClientId;
import co.com.bancolombia.commons.enums.TechnicalExceptionEnum;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.contact.ContactRepository;
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
public class ClientRepositoryImplement
        extends AdapterOperations<Client, ClientData, ClientId, ClientRepository> {

    @Autowired
    private TimeFactory timeFactory;

    @Autowired
    public ClientRepositoryImplement(ClientRepository repository, ContactMapper mapper) {
        super(repository, null, null);
    }

    public Mono<ClientData> findContact(ClientId client) {
        return repository.findById(client)
                .onErrorMap(e -> new TechnicalException(e, FIND_CONTACT_ERROR));
    }
}
