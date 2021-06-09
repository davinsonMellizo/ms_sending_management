package co.com.bancolombia.state;

import co.com.bancolombia.AdapterOperations;
import co.com.bancolombia.commons.enums.TechnicalExceptionEnum;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.contact.ContactRepository;
import co.com.bancolombia.contact.data.ContactData;
import co.com.bancolombia.contact.data.ContactMapper;
import co.com.bancolombia.drivenadapters.TimeFactory;
import co.com.bancolombia.model.client.Client;
import co.com.bancolombia.model.contact.Contact;
import co.com.bancolombia.model.contact.gateways.ContactGateway;
import co.com.bancolombia.model.state.State;
import co.com.bancolombia.model.state.gateways.StateGateway;
import co.com.bancolombia.state.data.StateData;
import co.com.bancolombia.state.data.StateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class StateRepositoryImplement
        extends AdapterOperations<State, StateData, String, StateRepository>
        implements StateGateway {


    @Autowired
    public StateRepositoryImplement(StateRepository repository, StateMapper mapper) {
        super(repository, null, mapper::toEntity);
    }

    @Override
    public Mono<State> findStateByName(String name) {
        return doQuery(repository.findById(name))
                .onErrorMap(e -> new TechnicalException(e, TechnicalExceptionEnum.FIND_STATE_ERROR));
    }
}
