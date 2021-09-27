package co.com.bancolombia.provider;

import co.com.bancolombia.AdapterOperations;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.config.model.provider.Provider;
import co.com.bancolombia.config.model.provider.gateways.ProviderGateway;
import co.com.bancolombia.config.model.response.StatusResponse;
import co.com.bancolombia.drivenadapters.TimeFactory;
import co.com.bancolombia.provider.data.ProviderData;
import co.com.bancolombia.provider.data.ProviderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.List;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.*;

@Repository
public class ProviderRepositoryImplement
        extends AdapterOperations<Provider, ProviderData, String, ProviderRepository>
        implements ProviderGateway {

    @Autowired
    private TimeFactory timeFactory;

    @Autowired
    public ProviderRepositoryImplement(ProviderRepository repository, ProviderMapper mapper) {
        super(repository, mapper::toData, mapper::toEntity);
    }


    @Override
    public Mono<List<Provider>> findAll() {
        return repository.findAll()
                .map(this::convertToEntity)
                .collectList()
                .onErrorMap(e -> new TechnicalException(e, FIND_ALL_PROVIDERS_ERROR));
    }

    @Override
    public Mono<Provider> findProviderById(String id) {
        return doQuery(repository.findById(id))
                .onErrorMap(e -> new TechnicalException(e, FIND_PROVIDER_BY_ID_ERROR));
    }

    @Override
    public Mono<Provider> saveProvider(Provider provider) {
        return Mono.just(provider)
                .map(this::convertToData)
                .map(providerData -> providerData.toBuilder()
                        .isNew(true)
                        .createdDate(timeFactory.now())
                        .build())
                .flatMap(providerData -> repository.save(providerData))
                .map(this::convertToEntity)
                .onErrorMap(e -> new TechnicalException(e, SAVE_PROVIDER_ERROR));
    }

    @Override
    public Mono<StatusResponse<Provider>> updateProvider(Provider provider) {
        return this.findProviderById(provider.getId())
                .map(providerFound -> StatusResponse.<Provider>builder()
                        .before(providerFound)
                        .actual(provider)
                        .build())
                .flatMap(this::update);
    }

    private Mono<StatusResponse<Provider>> update(StatusResponse<Provider> response) {
        return Mono.just(response.getActual())
                .map(this::convertToData)
                .map(data -> data.toBuilder().isNew(false).createdDate(response.getBefore().getCreatedDate()).build())
                .flatMap(repository::save)
                .map(this::convertToEntity)
                .map(actual -> response.toBuilder().actual(actual).description("Actualización Exitosa").build())
                .onErrorMap(e -> new TechnicalException(e, UPDATE_PROVIDER_ERROR));
    }

    @Override
    public Mono<String> deleteProviderById(String id) {
        return repository.deleteById(id)
                .onErrorMap(e -> new TechnicalException(e, DELETE_SERVICE_ERROR))
                .thenReturn(id);
    }
}
