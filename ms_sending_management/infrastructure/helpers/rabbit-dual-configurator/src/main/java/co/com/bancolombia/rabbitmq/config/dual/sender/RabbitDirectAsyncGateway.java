package co.com.bancolombia.rabbitmq.config.dual.sender;


import org.reactivecommons.api.domain.Command;
import org.reactivecommons.async.api.AsyncQuery;
import org.reactivecommons.async.api.From;
import org.reactivecommons.async.commons.config.BrokerConfig;
import org.reactivecommons.async.commons.converters.MessageConverter;
import org.reactivecommons.async.commons.reply.ReactiveReplyRouter;
import org.reactivecommons.async.rabbit.communications.ReactiveMessageSender;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.OutboundMessageResult;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RabbitDirectAsyncGateway implements DirectAsyncDualGateway {
    private final BrokerConfig config;
    private final ReactiveReplyRouter router;
    private final ReactiveMessageSender sender;
    private final String exchange;
    private final MessageConverter converter;
    private final boolean persistentCommands;
    private final boolean persistentQueries;
    private final Duration replyTimeout;

    public RabbitDirectAsyncGateway(BrokerConfig config, ReactiveReplyRouter router, ReactiveMessageSender sender,
                                    String exchange, MessageConverter converter) {
        this.config = config;
        this.router = router;
        this.sender = sender;
        this.exchange = exchange;
        this.converter = converter;
        this.persistentCommands = config.isPersistentCommands();
        this.persistentQueries = config.isPersistentQueries();
        this.replyTimeout = config.getReplyTimeout();
    }

    public <T> Mono<Void> sendCommand(Command<T> command, String targetName) {
        return this.sender.sendWithConfirm(command, this.exchange, targetName, Collections.emptyMap(), this.persistentCommands);
    }

    public <T> Flux<OutboundMessageResult> sendCommands(Flux<Command<T>> commands, String targetName) {
        return this.sender.sendWithConfirmBatch(commands, this.exchange, targetName, Collections.emptyMap(), this.persistentCommands);
    }

    public <T, R> Mono<R> requestReply(AsyncQuery<T> query, String targetName, Class<R> type) {
        String correlationID = UUID.randomUUID().toString().replaceAll("-", "");
        Mono<R> replyHolder = this.router.register(correlationID)
                .timeout(this.replyTimeout).flatMap((s) -> Mono.fromCallable(() -> this.converter.readValue(s, type)));
        Map<String, Object> headers = new HashMap();
        headers.put("x-reply_id", this.config.getRoutingKey());
        headers.put("x-serveQuery-id", query.getResource());
        headers.put("x-correlation-id", correlationID);
        return this.sender.sendNoConfirm(query, this.exchange, targetName + ".query", headers, this.persistentQueries).then(replyHolder);
    }

    public <T> Mono<Void> reply(T response, From from) {
        HashMap<String, Object> headers = new HashMap();
        headers.put("x-correlation-id", from.getCorrelationID());
        if (response == null) {
            headers.put("x-empty-completion", Boolean.TRUE.toString());
        }

        return this.sender.sendNoConfirm(response, "globalReply", from.getReplyID(), headers, false);
    }
}

