package co.com.bancolombia.model.message.gateways;

import co.com.bancolombia.model.Request;
import reactor.core.publisher.Mono;

public interface MessageGateway {
    Mono<Boolean> sendAlert(Request request);
}
