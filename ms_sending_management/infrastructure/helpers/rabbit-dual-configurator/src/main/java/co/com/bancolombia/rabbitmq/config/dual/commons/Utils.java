package co.com.bancolombia.rabbitmq.config.dual.commons;

import co.com.bancolombia.model.log.LoggerBuilder;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.experimental.UtilityClass;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@UtilityClass
public class Utils {
    private static final Integer BACK_OF_DURATION = 300;
    private static final Integer MAX_BACKOFF_DURATION = 3000;
    public static Mono<Connection> createConnectionMono(ConnectionFactory factory, String connectionPrefix,
                                                        String connectionType, LoggerBuilder logger) {
        return Mono.fromCallable(() -> factory.newConnection(connectionPrefix + " " + connectionType))
                .doOnError(logger::error)
                .retryWhen(Retry.backoff(Long.MAX_VALUE, Duration.ofMillis(BACK_OF_DURATION))
                        .maxBackoff(Duration.ofMillis(MAX_BACKOFF_DURATION)))
                .cache();
    }
}
