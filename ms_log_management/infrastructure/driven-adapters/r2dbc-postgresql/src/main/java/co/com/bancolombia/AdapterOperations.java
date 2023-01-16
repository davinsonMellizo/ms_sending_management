package co.com.bancolombia;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public class AdapterOperations<E, D, I, R extends ReactiveCrudRepository<D, I>, O extends ReactiveCrudRepository<D, I>> {

    protected R repository;
    protected O repositoryRead;
    protected Function<E, D> toData;
    protected Function<D, E> toEntity;

    @SuppressWarnings("unchecked")
    public AdapterOperations(R repository, O repositoryRead, Function<E, D> toData, Function<D, E> toEntity) {
        this.repository = repository;
        this.repositoryRead = repositoryRead;
        this.toData = toData;
        this.toEntity = toEntity;
    }

    protected D convertToData(E entity) {
        return toData.apply(entity);
    }

}
