package co.com.bancolombia;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public class AdapterOperations<E, D, I, R extends ReactiveCrudRepository<D, I>> {

    protected R repository;
    protected Function<E, D> toData;
    protected Function<D, E> toEntity;

    @SuppressWarnings("unchecked")
    public AdapterOperations(R repository, Function<E, D> toData, Function<D, E> toEntity) {
        this.repository = repository;
        this.toData = toData;
        this.toEntity = toEntity;
    }

    public Mono<E> save(E entity) {
        return Mono.just(entity)
                .map(this::convertToData)
                .flatMap(this::saveData)
                .thenReturn(entity);
    }

    protected Mono<E> doQuery(Mono<D> query) {
        return query.map(this::convertToEntity);
    }

    protected D convertToData(E entity) {
        return toData.apply(entity);
    }

    protected E convertToEntity(D data) {
        return toEntity.apply(data);
    }

    protected Mono<D> saveData(D data) {
        return repository.save(data);
    }

    protected Mono<Void> deleteById(I id) {
        return repository.deleteById(id);
    }

}
