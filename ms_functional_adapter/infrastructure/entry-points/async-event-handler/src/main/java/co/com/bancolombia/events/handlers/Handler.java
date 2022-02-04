package co.com.bancolombia.events.handlers;

import co.com.bancolombia.commons.utils.JsonUtils;
import co.com.bancolombia.events.model.ResourceQuery;
import co.com.bancolombia.model.transaction.Transaction;
import co.com.bancolombia.s3bucket.S3AsynOperations;
import co.com.bancolombia.usecase.functionaladapter.FunctionalAdapterUseCase;
import lombok.AllArgsConstructor;
import org.reactivecommons.api.domain.Command;
import org.reactivecommons.async.impl.config.annotations.EnableCommandListeners;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;

@AllArgsConstructor
@EnableCommandListeners
@Component
public class Handler {


    private final S3AsynOperations s3AsynOperations;
    private final FunctionalAdapterUseCase useCase;

    public Mono<Void> handleSendAlert(Command<String> stringCommand, String configMQKey, String bucketName) {
        System.out.println("Legga con "+ configMQKey);
        return s3AsynOperations.getFileAsString(bucketName, configMQKey)
                .map(s -> JsonUtils.stringToType(s, ResourceQuery.class))
                .map(ResourceQuery::getData)
                .flatMapMany(resources -> Flux.fromIterable(resources))
                .map(resource -> Transaction.builder()
                        .channel(resource.getChannel())
                        .nroTransaction(resource.getTransaction())
                        .template("message template ")
                        .payload(new HashMap<>()).build())
                .map(transaction -> useCase.sendTransactionToMQ(transaction))
                .thenEmpty(Mono.empty());
    }
}
