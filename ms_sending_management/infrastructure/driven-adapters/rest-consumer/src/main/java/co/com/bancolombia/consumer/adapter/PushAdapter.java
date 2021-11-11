package co.com.bancolombia.consumer.adapter;

import co.com.bancolombia.model.message.PUSH;
import co.com.bancolombia.model.message.Response;
import co.com.bancolombia.model.message.gateways.PushGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class PushAdapter implements PushGateway {

    @Override
    public Mono<Response> sendPush(PUSH push) {
        return null;
    }
}
