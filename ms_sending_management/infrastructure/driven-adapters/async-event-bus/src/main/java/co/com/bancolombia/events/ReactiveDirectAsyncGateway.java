package co.com.bancolombia.events;

import co.com.bancolombia.model.events.gateways.CommandGateway;
import co.com.bancolombia.model.message.Mail;
import co.com.bancolombia.model.message.Response;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.reactivecommons.api.domain.Command;
import org.reactivecommons.async.api.AsyncQuery;
import org.reactivecommons.async.api.DirectAsyncGateway;
import org.reactivecommons.async.impl.config.annotations.EnableDirectAsyncGateway;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.logging.Level;

@AllArgsConstructor
@EnableDirectAsyncGateway
public class ReactiveDirectAsyncGateway implements CommandGateway {
    public static final String TARGET_NAME = "ms_email_management";
    public static final String SEND_ALERT_EMAIL = "send.alert.email";
    private final DirectAsyncGateway gateway;


    @Override
    public Mono<Response> sendCommandAlertEmail(Mail mail) {
        Command command = new Command<>(SEND_ALERT_EMAIL, UUID.randomUUID().toString(), mail);
        return gateway.sendCommand(command,TARGET_NAME)
                .then(Mono.empty());
    }
}
