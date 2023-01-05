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

    public static Mono<Connection> createConnectionMono(ConnectionFactory factory, String connectionPrefix,
                                                        String connectionType, LoggerBuilder logger) {
        return Mono.fromCallable(() -> factory.newConnection(connectionPrefix + " " + connectionType))
                .doOnError(err ->
                        logger.error(err)
                )
                .retryWhen(Retry.backoff(Long.MAX_VALUE, Duration.ofMillis(300))
                        .maxBackoff(Duration.ofMillis(3000)))
                .cache();
    }
}
