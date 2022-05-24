package co.com.bancolombia.model.token;

import reactor.core.publisher.Mono;

public interface SecretGateway {
    Mono<Account> getSecretName(String priorityProvider);
}
