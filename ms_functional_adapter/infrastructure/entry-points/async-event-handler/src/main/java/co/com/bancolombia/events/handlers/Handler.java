package co.com.bancolombia.events.handlers;

import co.com.bancolombia.commons.utils.JsonUtils;
import co.com.bancolombia.events.model.ResourceQuery;
import co.com.bancolombia.model.transaction.Transaction;
import co.com.bancolombia.s3bucket.S3AsynOperations;
import co.com.bancolombia.usecase.functionaladapter.FunctionalAdapterUseCase;
import lombok.AllArgsConstructor;
import org.reactivecommons.api.domain.Command;
import org.reactivecommons.async.api.HandlerRegistry;
import org.reactivecommons.async.impl.config.annotations.EnableCommandListeners;
import org.springframework.beans.factory.annotation.Value;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@EnableCommandListeners
public class Handler {

    private final FunctionalAdapterUseCase useCase;

    public Mono<Void> handleSendAlert(Transaction transaction) {
        return useCase.sendTransactionToMQ(transaction);
    }
}
