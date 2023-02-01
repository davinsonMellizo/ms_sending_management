package co.com.bancolombia.newness;

import co.com.bancolombia.newness.data.NewnessData;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface INewnessRepository extends ReactiveCrudRepository<NewnessData, Integer> {

}
