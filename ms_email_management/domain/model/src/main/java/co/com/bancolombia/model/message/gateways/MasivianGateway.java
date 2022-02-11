package co.com.bancolombia.model.message.gateways;

import co.com.bancolombia.model.message.Mail;
import co.com.bancolombia.model.message.Response;
import reactor.core.publisher.Mono;

public interface MasivianGateway {
    Mono<Response> sendMAIL(Mail mail);
}
