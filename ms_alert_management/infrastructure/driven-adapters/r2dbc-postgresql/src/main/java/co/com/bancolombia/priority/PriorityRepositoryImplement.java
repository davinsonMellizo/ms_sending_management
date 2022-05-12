package co.com.bancolombia.priority;

import co.com.bancolombia.AdapterOperations;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.drivenadapters.TimeFactory;
import co.com.bancolombia.model.priority.Priority;
import co.com.bancolombia.model.priority.gateways.PriorityGateway;
import co.com.bancolombia.model.response.StatusResponse;
import co.com.bancolombia.priority.data.PriorityData;
import co.com.bancolombia.priority.data.PriorityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.List;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.*;

@Repository
public class PriorityRepositoryImplement extends AdapterOperations<Priority, PriorityData, Integer, PriorityRepository>
        implements PriorityGateway {

    @Autowired
    private TimeFactory timeFactory;

    @Autowired
    public PriorityRepositoryImplement(PriorityRepository repository, PriorityMapper mapper) {
        super(repository, mapper::toData, mapper::toEntity);
    }

    @Override
    public Mono<List<Priority>> findAllByProvider(String idProvider) {
        return repository.findAllByProviderId(idProvider)
                .map(this::convertToEntity)
                .collectList()
                .onErrorMap(e -> new TechnicalException(e, FIND_ALL_PROVIDERS_ERROR));
    }

    @Override
    public Mono<Priority> findPriorityById(Integer id) {
        return doQuery(repository.findById(id))
                .onErrorMap(e -> new TechnicalException(e, FIND_PROVIDER_BY_ID_ERROR));
    }

    @Override
    public Mono<Priority> savePriority(Priority priority) {
        return Mono.just(priority)
                .map(this::convertToData)
                .map(priorityData -> priorityData.toBuilder()
                        .isNew(true)
                        .createdDate(timeFactory.now())
                        .build())
                .flatMap(priorityData -> repository.save(priorityData))
                .map(this::convertToEntity)
                .onErrorMap(e -> new TechnicalException(e, SAVE_PROVIDER_ERROR));
    }

    @Override
    public Mono<StatusResponse<Priority>> updatePriority(Priority priority) {
        return this.findPriorityById(priority.getId())
                .map(priority1 -> StatusResponse.<Priority>builder()
                        .before(priority1)
                        .actual(priority)
                        .build())
                .flatMap(this::update);
    }

    private Mono<StatusResponse<Priority>> update(StatusResponse<Priority> response) {
        return Mono.just(response.getActual())
                .map(this::convertToData)
                .map(data -> data.toBuilder().isNew(false).createdDate(response.getBefore().getCreatedDate()).build())
                .flatMap(repository::save)
                .map(this::convertToEntity)
                .map(actual -> response.toBuilder().actual(actual).description("Actualización Exitosa").build())
                .onErrorMap(e -> new TechnicalException(e, UPDATE_PROVIDER_ERROR));
    }

    @Override
    public Mono<Integer> deletePriorityById(Integer id) {
        return repository.deleteById(id)
                .onErrorMap(e -> new TechnicalException(e, DELETE_PROVIDER_ERROR))
                .thenReturn(id);
    }
}