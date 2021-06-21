package co.com.bancolombia.remitter;

import co.com.bancolombia.remitter.data.RemitterData;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface RemitterRepository extends ReactiveCrudRepository<RemitterData, Integer> {

}
