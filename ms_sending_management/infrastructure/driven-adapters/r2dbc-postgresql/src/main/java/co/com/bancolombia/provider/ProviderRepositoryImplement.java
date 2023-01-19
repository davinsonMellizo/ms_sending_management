package co.com.bancolombia.provider;

import co.com.bancolombia.AdapterOperations;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.model.provider.Provider;
import co.com.bancolombia.model.provider.gateways.ProviderGateway;
import co.com.bancolombia.provider.data.ProviderData;
import co.com.bancolombia.provider.data.ProviderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.FIND_PROVIDER_BY_ID_ERROR;

@Repository
public class ProviderRepositoryImplement
        extends AdapterOperations<Provider, ProviderData, String, ProviderRepository>
        implements ProviderGateway {

    @Autowired
    public ProviderRepositoryImplement(ProviderRepository repository, ProviderMapper mapper) {
        super(repository, null, mapper::toEntity);
    }

    @Override
    public Mono<Provider> findProviderById(String id) {
        return doQuery(repository.findById(id))
                .onErrorMap(e -> new TechnicalException(e, FIND_PROVIDER_BY_ID_ERROR));
    }

}
