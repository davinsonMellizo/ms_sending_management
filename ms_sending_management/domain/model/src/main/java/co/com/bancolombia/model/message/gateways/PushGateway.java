package co.com.bancolombia.model.message.gateways;

import co.com.bancolombia.model.message.Push;
import co.com.bancolombia.model.message.Response;
import reactor.core.publisher.Mono;

public interface PushGateway {

    Mono<Response> sendPush(Push push);
}
