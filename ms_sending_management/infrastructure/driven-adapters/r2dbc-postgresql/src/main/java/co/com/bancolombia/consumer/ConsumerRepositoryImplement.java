package co.com.bancolombia.consumer;

import co.com.bancolombia.AdapterOperations;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.model.consumer.Consumer;
import co.com.bancolombia.model.consumer.gateways.ConsumerGateway;
import co.com.bancolombia.model.response.StatusResponse;
import co.com.bancolombia.consumer.data.ConsumerData;
import co.com.bancolombia.consumer.data.ConsumerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.List;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.*;

@Repository
public class ConsumerRepositoryImplement extends AdapterOperations<Consumer, ConsumerData, String, ConsumerRepository>
        implements ConsumerGateway {

    @Autowired
    public ConsumerRepositoryImplement(ConsumerRepository repository, ConsumerMapper mapper) {
        super(repository, mapper::toData, mapper::toEntity);
    }

    @Override
    public Mono<List<Consumer>> findAll() {
        return repository.findAll()
                .map(this::convertToEntity)
                .collectList()
                .onErrorMap(e -> new TechnicalException(e, FIND_ALL_CONSUMER_ERROR));
    }

    @Override
    public Mono<Consumer> findConsumerById(String id) {
        return doQuery(repository.findById(id))
                .onErrorMap(e -> new TechnicalException(e, FIND_CONSUMER_BY_ID_ERROR));
    }

    @Override
    public Mono<Consumer> saveConsumer(Consumer consumer) {

        return Mono.just(consumer)
                .map(this::convertToData)
                .map(consumerData -> consumerData.toBuilder()
                        .isNew(true)
                        .build())
                .flatMap(repository::save)
                .map(this::convertToEntity)
                .onErrorMap(e -> new TechnicalException(e, SAVE_CONSUMER_ERROR));
    }

    @Override
    public Mono<StatusResponse<Consumer>> updateConsumer(Consumer consumer) {
        return findConsumerById(consumer.getId())
                .map(consumerFound -> StatusResponse.<Consumer>builder()
                        .before(consumerFound)
                        .actual(consumer)
                        .build())
                .flatMap(this::update);
    }

    private Mono<StatusResponse<Consumer>> update(StatusResponse<Consumer> response) {
        return Mono.just(response.getActual())
                .map(this::convertToData)
                .map(data -> data.toBuilder().isNew(false).build())
                .flatMap(repository::save)
                .map(this::convertToEntity)
                .map(actual -> response.toBuilder().actual(actual).description("Actualización Exitosa").build())
                .onErrorMap(e -> new TechnicalException(e, UPDATE_CONSUMER_ERROR));
    }

    @Override
    public Mono<String> deleteConsumerById(String id) {
        return repository.deleteById(id)
                .onErrorMap(e -> new TechnicalException(e, DELETE_CONSUMER_ERROR))
                .thenReturn(id);
    }

}
