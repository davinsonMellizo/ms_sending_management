package co.com.bancolombia.model.message.gateways;

import co.com.bancolombia.model.message.Mail;
import co.com.bancolombia.model.message.Response;
import co.com.bancolombia.model.message.Sms;
import reactor.core.publisher.Mono;

public interface MasivianGateway {

    Mono<Response> sendSMS(Sms sms);

    Mono<Response> sendMAIL(Mail mail);
}
