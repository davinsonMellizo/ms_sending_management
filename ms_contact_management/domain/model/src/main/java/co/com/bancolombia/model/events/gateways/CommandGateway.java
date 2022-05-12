package co.com.bancolombia.model.events.gateways;

import co.com.bancolombia.model.bridge.Bridge;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface CommandGateway {

    Mono<Void> sendCommandEnroll(Bridge bridge);

    Mono<Void> sendCommandUpdate(Bridge bridge);
}
