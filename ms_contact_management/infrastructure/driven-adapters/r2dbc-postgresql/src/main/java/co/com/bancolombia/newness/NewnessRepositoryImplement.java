package co.com.bancolombia.newness;

import co.com.bancolombia.AdapterOperations;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.drivenadapters.TimeFactory;
import co.com.bancolombia.model.newness.Newness;
import co.com.bancolombia.model.newness.gateways.NewnessRepository;
import co.com.bancolombia.newness.data.NewnessData;
import co.com.bancolombia.newness.data.NewnessMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.SAVE_CLIENT_ERROR;


@Repository
public class NewnessRepositoryImplement
        extends AdapterOperations<Newness, NewnessData, Integer, INewnessRepository>
        implements NewnessRepository {

    @Autowired
    private TimeFactory timeFactory;

    public NewnessRepositoryImplement(INewnessRepository repository, NewnessMapper mapper) {
        super(repository, mapper::toData, null);
    }

    @Override
    public Mono<Newness> saveNewness(Newness newness) {
        return Mono.just(newness.toBuilder().dateCreation(timeFactory.now()).build())
                .map(this::convertToData)
                .flatMap(repository::save)
                .thenReturn(newness);
    }
}
