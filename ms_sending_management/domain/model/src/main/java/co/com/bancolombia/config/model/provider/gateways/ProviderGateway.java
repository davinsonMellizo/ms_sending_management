package co.com.bancolombia.config.model.provider.gateways;

import co.com.bancolombia.config.model.provider.Provider;
import co.com.bancolombia.config.model.response.StatusResponse;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ProviderGateway {
    Mono<List<Provider>> findAll();

    Mono<Provider> findProviderById(String id);

    Mono<Provider> saveProvider(Provider provider);

    Mono<StatusResponse<Provider>> updateProvider(Provider provider);

    Mono<String> deleteProviderById(String id);
}
