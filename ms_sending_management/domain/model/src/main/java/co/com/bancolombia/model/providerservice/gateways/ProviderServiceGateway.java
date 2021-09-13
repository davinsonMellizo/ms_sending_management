package co.com.bancolombia.model.providerservice.gateways;

import co.com.bancolombia.model.providerservice.ProviderService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProviderServiceGateway {

    Mono<ProviderService> saveProviderService(ProviderService providerService);

    Flux<ProviderService> findAllProviderService();

    Mono<Integer> deleteProviderService(ProviderService providerService);
}
