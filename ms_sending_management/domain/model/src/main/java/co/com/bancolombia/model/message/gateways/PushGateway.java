package co.com.bancolombia.model.message.gateways;

import co.com.bancolombia.model.message.PUSH;
import co.com.bancolombia.model.message.Response;
import reactor.core.publisher.Mono;

public interface PushGateway {

    Mono<Response> sendPush(PUSH push);
}
