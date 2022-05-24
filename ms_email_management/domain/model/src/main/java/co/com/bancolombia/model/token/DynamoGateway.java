package co.com.bancolombia.model.token;

import reactor.core.publisher.Mono;

public interface DynamoGateway {
    Mono<Secret> getTokenName(String priorityProvider);
}
