package co.com.bancolombia.config.model.message.gateways;

import co.com.bancolombia.config.model.message.Mail;
import reactor.core.publisher.Mono;

public interface MessageGateway {
    Mono<Void> sendEmail(Mail mail);
}
