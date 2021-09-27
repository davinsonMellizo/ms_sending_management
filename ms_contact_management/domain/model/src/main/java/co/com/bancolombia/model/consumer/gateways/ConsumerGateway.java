package co.com.bancolombia.model.consumer.gateways;

import co.com.bancolombia.model.consumer.Consumer;
import co.com.bancolombia.model.response.StatusResponse;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ConsumerGateway {

      Mono<Consumer> findConsumerById(String id);

}
