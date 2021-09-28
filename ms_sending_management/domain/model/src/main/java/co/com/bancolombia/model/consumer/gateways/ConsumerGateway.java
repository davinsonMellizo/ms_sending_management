package co.com.bancolombia.model.consumer.gateways;

import co.com.bancolombia.model.consumer.Consumer;
import co.com.bancolombia.model.response.StatusResponse;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ConsumerGateway {

    Mono<List<Consumer>> findAll();

    Mono<Consumer> findConsumerById(String id);

    Mono<Consumer> saveConsumer(Consumer consumer);

    Mono<StatusResponse<Consumer>> updateConsumer(Consumer consumer);

    Mono<String> deleteConsumerById(String id);
}
