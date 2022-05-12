package co.com.bancolombia.newness;

import co.com.bancolombia.AdapterOperations;
import co.com.bancolombia.drivenadapters.TimeFactory;
import co.com.bancolombia.model.newness.Newness;
import co.com.bancolombia.model.newness.gateways.NewnessRepository;
import co.com.bancolombia.newness.data.NewnessData;
import co.com.bancolombia.newness.data.NewnessMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;


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
    public Mono<Newness> saveNewness(List<Newness> newness) {
        return Flux.fromIterable(newness)
                .map(news -> news.toBuilder().dateCreation(timeFactory.now()).build())
                .map(this::convertToData)
                .collectList()
                .flatMapMany(repository::saveAll)
                .then(Mono.just(newness.get(0)));
    }
}
