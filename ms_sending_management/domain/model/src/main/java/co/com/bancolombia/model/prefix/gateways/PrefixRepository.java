package co.com.bancolombia.model.prefix.gateways;

import co.com.bancolombia.model.prefix.Prefix;
import reactor.core.publisher.Mono;

public interface PrefixRepository {
    Mono<Prefix> findPrefix(String code);
}
