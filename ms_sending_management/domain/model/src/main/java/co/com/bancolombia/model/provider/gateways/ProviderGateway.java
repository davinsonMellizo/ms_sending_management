package co.com.bancolombia.model.provider.gateways;

import co.com.bancolombia.model.provider.Provider;
import co.com.bancolombia.model.response.StatusResponse;
import reactor.core.publisher.Mono;

public interface ProviderGateway {
    Mono<Provider> findProviderById(String id);
    Mono<Provider> saveProvider(Provider Provider);
    Mono<StatusResponse<Provider>> updateProvider(Provider Provider);
    Mono<String> deleteProviderById(String id);
}
