package co.com.bancolombia.model.message.gateways;

import co.com.bancolombia.model.message.TemplateSms;
import reactor.core.publisher.Mono;

public interface PinpointGateway {
    Mono<TemplateSms> findTemplateSms(String templateName);
}
