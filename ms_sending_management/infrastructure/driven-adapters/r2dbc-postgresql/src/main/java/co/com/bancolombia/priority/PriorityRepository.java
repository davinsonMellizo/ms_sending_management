package co.com.bancolombia.priority;

import co.com.bancolombia.priority.data.PriorityData;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface PriorityRepository extends ReactiveCrudRepository<PriorityData, Integer> {

    @Query("SELECT pri.* FROM priority pri JOIN provider pro ON pri.id_provider = pro.id " +
            "WHERE pro.id = $1")
    Flux<PriorityData> findAllByProviderId(String idProvider);
}
