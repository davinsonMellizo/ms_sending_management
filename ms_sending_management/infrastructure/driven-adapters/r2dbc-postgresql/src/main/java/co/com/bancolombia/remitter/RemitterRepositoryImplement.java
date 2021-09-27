package co.com.bancolombia.remitter;


import co.com.bancolombia.AdapterOperations;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.config.model.remitter.Remitter;
import co.com.bancolombia.config.model.remitter.gateways.RemitterGateway;
import co.com.bancolombia.config.model.response.StatusResponse;
import co.com.bancolombia.drivenadapters.TimeFactory;
import co.com.bancolombia.remitter.data.RemitterData;
import co.com.bancolombia.remitter.data.RemitterMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.List;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.*;

@Repository
public class RemitterRepositoryImplement
        extends AdapterOperations<Remitter, RemitterData, Integer, RemitterRepository>
        implements RemitterGateway {

    @Autowired
    private TimeFactory timeFactory;

    @Autowired
    public RemitterRepositoryImplement(RemitterRepository repository, RemitterMapper mapper) {
        super(repository, mapper::toData, mapper::toEntity);
    }


    @Override
    public Mono<List<Remitter>> findAll() {
        return repository.findAll()
                .map(this::convertToEntity)
                .collectList()
                .onErrorMap(e -> new TechnicalException(e, FIND_ALL_REMITTERS_ERROR));
    }

    @Override
    public Mono<Remitter> findRemitterById(Integer id) {
        return doQuery(repository.findById(id))
                .onErrorMap(e -> new TechnicalException(e, FIND_REMITTER_BY_ID_ERROR));
    }

    @Override
    public Mono<Remitter> saveRemitter(Remitter remitter) {
        return Mono.just(remitter)
                .map(this::convertToData)
                .map(remitterData -> remitterData.toBuilder()
                        .isNew(true)
                        .createdDate(timeFactory.now())
                        .build())
                .flatMap(repository::save)
                .map(this::convertToEntity)
                .onErrorMap(e -> new TechnicalException(e, SAVE_SERVICE_ERROR));
    }

    @Override
    public Mono<StatusResponse<Remitter>> updateRemitter(Remitter remitter) {
        return findRemitterById(remitter.getId())
                .map(remitterFound -> StatusResponse.<Remitter>builder()
                        .before(remitterFound)
                        .actual(remitter)
                        .build())
                .flatMap(this::update);
    }

    private Mono<StatusResponse<Remitter>> update(StatusResponse<Remitter> response) {
        return Mono.just(response.getActual())
                .map(this::convertToData)
                .map(data -> data.toBuilder().isNew(false).createdDate(response.getBefore().getCreatedDate()).build())
                .flatMap(repository::save)
                .map(this::convertToEntity)
                .map(actual -> response.toBuilder().actual(actual).description("Actualización Exitosa").build())
                .onErrorMap(e -> new TechnicalException(e, UPDATE_REMITTER_ERROR));
    }

    @Override
    public Mono<Integer> deleteRemitterById(Integer id) {
        return repository.deleteById(id)
                .onErrorMap(e -> new TechnicalException(e, DELETE_REMITTER_ERROR))
                .thenReturn(id);
    }
}
