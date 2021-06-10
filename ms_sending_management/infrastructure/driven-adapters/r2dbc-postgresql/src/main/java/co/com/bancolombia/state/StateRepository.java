package co.com.bancolombia.state;

import co.com.bancolombia.state.data.StateData;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface StateRepository extends ReactiveCrudRepository<StateData, String> {

}
