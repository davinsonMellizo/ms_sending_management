package co.com.bancolombia.events.handlers;

import co.com.bancolombia.commandsmq.config.ListenerMQ;
import co.com.bancolombia.events.model.ResourceQuery;
import co.com.bancolombia.model.log.LoggerBuilder;
import co.com.bancolombia.model.transaction.Transaction;
import co.com.bancolombia.usecase.functionaladapter.FunctionalAdapterUseCase;
import lombok.AllArgsConstructor;
import org.reactivecommons.async.api.HandlerRegistry;
import org.reactivecommons.async.impl.config.annotations.EnableCommandListeners;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;

@AllArgsConstructor
@EnableCommandListeners
@Component
public class Handler {

    private final FunctionalAdapterUseCase useCase;
    private final LoggerBuilder loggerBuilder;

    public HandlerRegistry listenerMessage( ResourceQuery.Resource resource, HandlerRegistry register){
        return register.handleCommand(resource.getQueryName(), message -> Mono.just(message)
                .doOnNext(e -> loggerBuilder.info(e.getData()))
                .flatMap(command -> useCase.sendTransactionToMQ(Transaction.builder()
                        .template(resource.getTemplate())
                        .payload(command.getData())
                        .channel(resource.getChannel())
                        .nroTransaction(resource.getTransaction())
                        .build()))
                .then(), Map.class);
    }
}
