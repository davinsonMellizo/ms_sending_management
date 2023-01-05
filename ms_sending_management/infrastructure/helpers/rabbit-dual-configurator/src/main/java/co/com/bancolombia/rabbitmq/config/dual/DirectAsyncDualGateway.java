package co.com.bancolombia.rabbitmq.config.dual;

import org.reactivecommons.api.domain.Command;
import org.reactivecommons.async.api.AsyncQuery;
import org.reactivecommons.async.api.From;
import reactor.core.publisher.Mono;

public interface DirectAsyncDualGateway {
    <T> Mono<Void> sendCommand(Command<T> var1, String var2);

    <T, R> Mono<R> requestReply(AsyncQuery<T> var1, String var2, Class<R> var3);

    <T> Mono<Void> reply(T var1, From var2);
}
