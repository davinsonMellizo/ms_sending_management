package co.com.bancolombia.priority;

import co.com.bancolombia.AdapterOperations;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.model.priority.Priority;
import co.com.bancolombia.model.priority.gateways.PriorityGateway;
import co.com.bancolombia.priority.data.PriorityData;
import co.com.bancolombia.priority.data.PriorityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.FIND_PROVIDER_BY_ID_ERROR;

@Repository
public class PriorityRepositoryImplement extends AdapterOperations<Priority, PriorityData, Integer, PriorityRepository>
        implements PriorityGateway {

    @Autowired
    public PriorityRepositoryImplement(PriorityRepository repository, PriorityMapper mapper) {
        super(repository, mapper::toData, mapper::toEntity);
    }

    @Override
    public Mono<Priority> findPriorityById(Integer id) {
        return doQuery(repository.findById(id))
                .onErrorMap(e -> new TechnicalException(e, FIND_PROVIDER_BY_ID_ERROR));
    }

}