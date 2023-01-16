package co.com.bancolombia.remitter;


import co.com.bancolombia.AdapterOperations;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.drivenadapters.TimeFactory;
import co.com.bancolombia.model.remitter.Remitter;
import co.com.bancolombia.model.remitter.gateways.RemitterGateway;
import co.com.bancolombia.remitter.data.RemitterData;
import co.com.bancolombia.remitter.data.RemitterMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.FIND_REMITTER_BY_ID_ERROR;

@Repository
public class RemitterRepositoryImplement
        extends AdapterOperations<Remitter, RemitterData, Integer, RemitterRepository>
        implements RemitterGateway {

    @Autowired
    private TimeFactory timeFactory;

    @Autowired
    public RemitterRepositoryImplement(RemitterRepository repository, RemitterMapper mapper) {
        super(repository, null, mapper::toEntity);
    }

    @Override
    public Mono<Remitter> findRemitterById(Integer id) {
        return doQuery(repository.findById(id))
                .onErrorMap(e -> new TechnicalException(e, FIND_REMITTER_BY_ID_ERROR));
    }

}
