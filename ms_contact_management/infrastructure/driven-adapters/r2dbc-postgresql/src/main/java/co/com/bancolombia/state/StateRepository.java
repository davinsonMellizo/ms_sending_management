package co.com.bancolombia.state;

import co.com.bancolombia.state.data.StateData;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface StateRepository extends ReactiveCrudRepository<StateData, String> {
    @Query("select * from state where lower(name) = lower($1) or id::text = $1")
    Mono<StateData> findState(String data);

}
