package co.com.bancolombia.model.message.gateways;

import co.com.bancolombia.model.message.TemplateEmail;
import reactor.core.publisher.Mono;

public interface PinpointGateway {
    Mono<TemplateEmail> findTemplateEmail(String templateName);
}
