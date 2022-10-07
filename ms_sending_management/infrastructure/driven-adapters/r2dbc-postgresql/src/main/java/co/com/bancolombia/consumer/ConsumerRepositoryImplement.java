package co.com.bancolombia.consumer;

import co.com.bancolombia.AdapterOperations;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.consumer.data.ConsumerData;
import co.com.bancolombia.consumer.data.ConsumerMapper;
import co.com.bancolombia.model.consumer.Consumer;
import co.com.bancolombia.model.consumer.gateways.ConsumerGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.FIND_CONSUMER_BY_ID_ERROR;

@Repository
public class ConsumerRepositoryImplement extends AdapterOperations<Consumer, ConsumerData, String, ConsumerRepository>
        implements ConsumerGateway {

    @Autowired
    public ConsumerRepositoryImplement(ConsumerRepository repository, ConsumerMapper mapper) {
        super(repository, null, mapper::toEntity);
    }

    @Override
    public Mono<Consumer> findConsumerById(String id) {
        return doQuery(repository.findById(id))
                .onErrorMap(e -> new TechnicalException(e, FIND_CONSUMER_BY_ID_ERROR));
    }

}
