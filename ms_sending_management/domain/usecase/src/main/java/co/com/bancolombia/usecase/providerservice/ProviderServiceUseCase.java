package co.com.bancolombia.usecase.providerservice;

import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.providerservice.ProviderService;
import co.com.bancolombia.model.providerservice.gateways.ProviderServiceGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.List;

import static co.com.bancolombia.commons.enums.BusinessErrorMessage.PROVIDER_SERVICE_NOT_FOUND;

@RequiredArgsConstructor
public class ProviderServiceUseCase {

    private final ProviderServiceGateway providerServiceGateway;

    public Mono<ProviderService> saveProviderService(ProviderService providerService) {
        return providerServiceGateway.saveProviderService(providerService);
    }

    public Mono<Integer> deleteProviderService(ProviderService providerService) {
        return providerServiceGateway.deleteProviderService(providerService)
                .switchIfEmpty(Mono.error(new BusinessException(PROVIDER_SERVICE_NOT_FOUND)));
    }

    public Mono<List<ProviderService>> findAllProviderService() {
        return providerServiceGateway.findAllProviderService()
                .switchIfEmpty(Mono.error(new BusinessException(PROVIDER_SERVICE_NOT_FOUND)))
                .collectList();
    }
}
