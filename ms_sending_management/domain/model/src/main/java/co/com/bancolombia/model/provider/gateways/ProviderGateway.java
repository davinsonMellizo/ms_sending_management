package co.com.bancolombia.model.provider.gateways;

import co.com.bancolombia.model.provider.Provider;
import reactor.core.publisher.Mono;

public interface ProviderGateway {
    Mono<Provider> findProviderById(String id);
}
