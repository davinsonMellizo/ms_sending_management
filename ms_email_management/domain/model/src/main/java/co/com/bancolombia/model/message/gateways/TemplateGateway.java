package co.com.bancolombia.model.message.gateways;

import co.com.bancolombia.model.message.Alert;
import co.com.bancolombia.model.message.TemplateEmail;
import reactor.core.publisher.Mono;

public interface TemplateGateway {

    Mono<TemplateEmail> findTemplateEmail(Alert alert);
}
