package co.com.bancolombia.model.events.gateways;

import co.com.bancolombia.model.message.Mail;
import co.com.bancolombia.model.message.Response;
import co.com.bancolombia.model.message.Sms;
import reactor.core.publisher.Mono;

public interface CommandGateway {
    Mono<Response> sendCommandAlertEmail(Mail mail);

    Mono<Response> sendCommandAlertSms(Sms sms);
}
