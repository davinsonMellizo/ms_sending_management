package co.com.bancolombia.events;

import co.com.bancolombia.model.events.gateways.CommandGateway;
import co.com.bancolombia.model.log.Log;
import co.com.bancolombia.model.message.Mail;
import co.com.bancolombia.model.message.Response;
import co.com.bancolombia.model.message.Sms;
import lombok.AllArgsConstructor;
import org.reactivecommons.api.domain.Command;
import org.reactivecommons.async.api.DirectAsyncGateway;
import org.reactivecommons.async.impl.config.annotations.EnableDirectAsyncGateway;
import reactor.core.publisher.Mono;

import java.util.UUID;

@AllArgsConstructor
@EnableDirectAsyncGateway
public class ReactiveDirectAsyncGateway implements CommandGateway {
    public static final String TARGET_NAME_EMAIL = "ms_email_management";
    public static final String TARGET_NAME_SMS = "ms_sms_management";
    public static final String TARGET_NAME_LOG = "ms_log_management";
    public static final String SEND_ALERT_EMAIL = "send.alert.email";
    public static final String SEND_ALERT_LOG = "save.log.send.alert";
    public static final String SEND_ALERT_SMS = "send.alert.sms";
    private final DirectAsyncGateway gateway;

    @Override
    public Mono<Response> sendCommandAlertSms(Sms sms) {
        Command command = new Command<>(SEND_ALERT_SMS, UUID.randomUUID().toString(), sms);
        return gateway.sendCommand(command,TARGET_NAME_SMS)
                .then(Mono.empty());
    }


    @Override
    public Mono<Response> sendCommandAlertEmail(Mail mail) {
        Command command = new Command<>(SEND_ALERT_EMAIL, UUID.randomUUID().toString(), mail);
        return gateway.sendCommand(command,TARGET_NAME_EMAIL)
                .then(Mono.empty());
    }

    @Override
    public  Mono<Log> sendCommandLogAlert(Log log){
        Command command = new Command<>(SEND_ALERT_LOG, UUID.randomUUID().toString(), log);
        return gateway.sendCommand(command,TARGET_NAME_LOG)
                .then(Mono.empty());
    }

}
