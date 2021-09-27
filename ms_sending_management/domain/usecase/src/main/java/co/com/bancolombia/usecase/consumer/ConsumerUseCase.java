package co.com.bancolombia.usecase.consumer;

import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.config.model.consumer.Consumer;
import co.com.bancolombia.config.model.consumer.gateways.ConsumerGateway;
import co.com.bancolombia.config.model.response.StatusResponse;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.List;

import static co.com.bancolombia.commons.enums.BusinessErrorMessage.CONSUMER_NOT_FOUND;

@RequiredArgsConstructor
public class ConsumerUseCase {

    private final ConsumerGateway consumerGateway;

    public Mono<List<Consumer>> findAllConsumer() {
        return consumerGateway.findAll()
                .filter(consumers -> !consumers.isEmpty())
                .switchIfEmpty(Mono.error(new BusinessException(CONSUMER_NOT_FOUND)));
    }

    public Mono<Consumer> findConsumerById(String id) {
        return consumerGateway.findConsumerById(id)
                .switchIfEmpty(Mono.error(new BusinessException(CONSUMER_NOT_FOUND)));
    }

    public Mono<Consumer> saveConsumer(Consumer consumer) {
        return consumerGateway.saveConsumer(consumer);
    }

    public Mono<StatusResponse<Consumer>> updateConsumer(Consumer consumer) {
        return consumerGateway.updateConsumer(consumer)
                .switchIfEmpty(Mono.error(new BusinessException(CONSUMER_NOT_FOUND)));
    }

    public Mono<String> deleteConsumerById(String id) {
        return consumerGateway.findConsumerById(id)
                .switchIfEmpty(Mono.error(new BusinessException(CONSUMER_NOT_FOUND)))
                .map(Consumer::getId)
                .flatMap(consumerGateway::deleteConsumerById);
    }
}
