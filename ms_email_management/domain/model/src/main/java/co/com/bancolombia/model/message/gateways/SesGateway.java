package co.com.bancolombia.model.message.gateways;

import co.com.bancolombia.model.message.Alert;
import co.com.bancolombia.model.message.Response;
import co.com.bancolombia.model.message.TemplateEmail;
import reactor.core.publisher.Mono;

public interface SesGateway {
    Mono<Response> sendEmail( TemplateEmail templateEmail,Alert alert);
}
