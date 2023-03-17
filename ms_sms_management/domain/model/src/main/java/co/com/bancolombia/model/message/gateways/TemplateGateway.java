package co.com.bancolombia.model.message.gateways;

import co.com.bancolombia.model.message.Alert;
import co.com.bancolombia.model.message.TemplateSms;
import reactor.core.publisher.Mono;

public interface TemplateGateway {
    Mono<TemplateSms> findTemplateEmail(Alert alert);
}
