package co.com.bancolombia.events;

import co.com.bancolombia.model.events.gateways.CommandGateway;
import co.com.bancolombia.model.log.Log;
import co.com.bancolombia.rabbitmq.config.dual.sender.DirectAsyncDualGateway;
import lombok.AllArgsConstructor;
import org.reactivecommons.api.domain.Command;
import org.reactivecommons.async.api.DirectAsyncGateway;
import org.reactivecommons.async.impl.config.annotations.EnableDirectAsyncGateway;
import reactor.core.publisher.Mono;

import java.util.UUID;


@AllArgsConstructor
@EnableDirectAsyncGateway
public class ReactiveDirectAsyncGateway implements CommandGateway {
    public static final String TARGET_NAME_LOG = "ms_log_management";
    public static final String SEND_ALERT_LOG = "save.log.send.alert";
    private final DirectAsyncDualGateway gateway;



    @Override
    public Mono<Log> sendCommandLogSms(Log log) {
        Command command= new Command<>(SEND_ALERT_LOG, UUID.randomUUID().toString(), log);
        return gateway.sendCommand(command,TARGET_NAME_LOG)
                .then(Mono.empty());
    }
}
