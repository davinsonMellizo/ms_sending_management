package co.com.bancolombia.providerservice;

import co.com.bancolombia.AdapterOperations;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.model.providerservice.ProviderService;
import co.com.bancolombia.model.providerservice.gateways.ProviderServiceGateway;
import co.com.bancolombia.providerservice.data.ProviderServiceData;
import co.com.bancolombia.providerservice.data.ProviderServiceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.*;

@Repository
public class ProviderServiceRepositoryImplement extends AdapterOperations<ProviderService, ProviderServiceData, Integer, ProviderServiceRepository>
        implements ProviderServiceGateway {

    @Autowired
    public ProviderServiceRepositoryImplement(ProviderServiceRepository repository, ProviderServiceMapper mapper) {
        super(repository, mapper::toData, mapper::toEntity);
    }

    @Override
    public Mono<ProviderService> saveProviderService(ProviderService providerService) {
        return Mono.just(providerService)
                .map(this::convertToData)
                .flatMap(repository::save)
                .map(this::convertToEntity)
                .onErrorMap(e -> new TechnicalException(e, SAVE_PROVIDER_SERVICE_ERROR));
    }

    @Override
    public Flux<ProviderService> findAllProviderService() {
        return repository.findAllProviderService()
                .map(this::convertToEntity)
                .onErrorMap(e -> new TechnicalException(e, FIND_PROVIDER_SERVICE_ERROR));
    }

    @Override
    public Mono<ProviderService> findProviderService(Integer integer) {
        return repository.findById(integer)
                .map(this::convertToEntity)
                .onErrorMap(e -> new TechnicalException(e, FIND_PROVIDER_SERVICE_ERROR));
    }

    @Override
    public Mono<Integer> deleteProviderService(ProviderService providerService) {
        return repository.deleteProviderService(providerService.getIdProvider(),
                providerService.getIdService())
                .filter(rowsAffected -> rowsAffected == 1)
                .map(integer -> providerService.getIdService())
                .onErrorMap(e -> new TechnicalException(e, DELETE_PROVIDER_SERVICE_ERROR));
    }
}
