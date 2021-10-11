package co.com.bancolombia.model.consumer.gateways;

import co.com.bancolombia.model.consumer.Consumer;
import reactor.core.publisher.Mono;

public interface ConsumerGateway {

      Mono<Consumer> findConsumerById(String id);

}
