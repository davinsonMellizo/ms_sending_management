package co.com.bancolombia.commandsmq.config;

import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.model.log.LoggerBuilder;
import co.com.bancolombia.model.transaction.Transaction;
import co.com.bancolombia.usecase.functionaladapter.FunctionalAdapterUseCase;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.time.LocalDateTime;

import static com.ibm.msg.client.jms.JmsConstants.JMSX_DELIVERY_COUNT;

@Primary
@Component
@RequiredArgsConstructor
public class ListenerMQ implements MessageListener {

    private final FunctionalAdapterUseCase useCase;
    private final LoggerBuilder loggerBuilder;
    private final Environment env;
    private static final String MAX_RETRIES = "app.async.maxRetries";
    private static final Integer META_DATA = 408;

    @SneakyThrows
    @Override
    public void onMessage(Message message) {
        loggerBuilder.info("sistema"+LocalDateTime.now());
        getMessage(message)
                .flatMap(useCase::sendTransactionToRabbit)
                .doOnError(e -> discard(e, message))
                .thenReturn("final del mensaje "+message.getBody(String.class) + LocalDateTime.now())
                .doOnNext(loggerBuilder::info)
                .subscribe();
    }

    private Mono<Transaction> getMessage(Message message) {
        try {
            return Mono.just(message.getBody(String.class))
                    .filter(payload -> payload.length()>META_DATA)
                    .map(payload -> payload.substring(META_DATA))
                    .map(payload -> Transaction.builder()
                            .payload(payload)
                            .build());
        } catch (JMSException e) {
            return Mono.error(e);
        }

    }

    private Mono<Void> discard(Throwable error, Message message) {
        loggerBuilder.info("descartar");
        final boolean isRetry = error instanceof TechnicalException
                && ((TechnicalException) error).getException().isRetry();

        final int maxRetries = env.getProperty(MAX_RETRIES, Integer.class);
        return Mono.defer(() -> {
            try {
                if (message.getIntProperty(JMSX_DELIVERY_COUNT) > maxRetries || isRetry) {
                    return Mono.empty();
                }
            } catch (JMSException e) {
                loggerBuilder.error(e);
            }
            return Mono.error(error);
        });
    }
}
