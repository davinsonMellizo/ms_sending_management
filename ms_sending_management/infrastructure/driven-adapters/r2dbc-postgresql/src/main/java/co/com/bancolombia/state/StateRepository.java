package co.com.bancolombia.state;

import co.com.bancolombia.contact.data.ContactData;
import co.com.bancolombia.state.data.StateData;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StateRepository extends ReactiveCrudRepository<StateData, String> {

}
