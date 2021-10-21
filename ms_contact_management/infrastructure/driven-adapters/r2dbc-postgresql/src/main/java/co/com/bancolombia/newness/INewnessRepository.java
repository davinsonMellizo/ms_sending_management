package co.com.bancolombia.newness;

import co.com.bancolombia.newness.data.NewnessData;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface INewnessRepository extends ReactiveCrudRepository<NewnessData, Integer> {
}
