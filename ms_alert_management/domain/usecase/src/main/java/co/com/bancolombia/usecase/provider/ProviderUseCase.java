package co.com.bancolombia.usecase.provider;

import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.provider.Provider;
import co.com.bancolombia.model.provider.gateways.ProviderGateway;
import co.com.bancolombia.model.response.StatusResponse;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.List;

import static co.com.bancolombia.commons.enums.BusinessErrorMessage.PROVIDER_NOT_FOUND;

@RequiredArgsConstructor
public class ProviderUseCase {
    private final ProviderGateway providerGateway;

    public Mono<List<Provider>> findAllProviders() {
         return providerGateway.findAll()
                .filter(providers -> !providers.isEmpty())
                .switchIfEmpty(Mono.error(new BusinessException(PROVIDER_NOT_FOUND)));
    }

    public Mono<Provider> findProviderById(String id) {
        return providerGateway.findProviderById(id)
                .switchIfEmpty(Mono.error(new BusinessException(PROVIDER_NOT_FOUND)));
    }

    public Mono<Provider> saveProvider(Provider provider) {
        return providerGateway.saveProvider(provider);
    }

    public Mono<StatusResponse<Provider>> updateProvider(Provider provider) {
        return providerGateway.updateProvider(provider)
                .switchIfEmpty(Mono.error(new BusinessException(PROVIDER_NOT_FOUND)));
    }

    public Mono<String> deleteProviderById(String id) {
        return providerGateway.findProviderById(id)
                .switchIfEmpty(Mono.error(new BusinessException(PROVIDER_NOT_FOUND)))
                .map(Provider::getId)
                .flatMap(providerGateway::deleteProviderById);
    }
}
