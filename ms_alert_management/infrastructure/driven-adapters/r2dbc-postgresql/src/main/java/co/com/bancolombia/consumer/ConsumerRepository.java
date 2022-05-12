package co.com.bancolombia.consumer;

import co.com.bancolombia.consumer.data.ConsumerData;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ConsumerRepository extends ReactiveCrudRepository<ConsumerData, String> {
}
