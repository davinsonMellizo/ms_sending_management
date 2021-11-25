package co.com.bancolombia.consumer.adapter;

import co.com.bancolombia.consumer.RestClient;
import co.com.bancolombia.consumer.adapter.response.Error;
import co.com.bancolombia.consumer.adapter.response.ErrorPush;
import co.com.bancolombia.consumer.adapter.response.SuccessPush;
import co.com.bancolombia.consumer.config.ConsumerProperties;
import co.com.bancolombia.model.message.Push;
import co.com.bancolombia.model.message.Response;
import co.com.bancolombia.model.message.gateways.PushGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class PushAdapter implements PushGateway {

    private final static Integer STATUS_OK = 202;
    private final static Integer STATUS_ERROR = 1;
    private final ConsumerProperties properties;
    private final RestClient<Push, SuccessPush> client;

    @Override
    public Mono<Response> sendPush(Push push) {
        System.out.println(push);
        return client.post(properties.getResources().getEndpointPush(), push,
                SuccessPush.class, ErrorPush.class)
                .map(response -> Response.builder().code(STATUS_OK)
                        .description(response.getData().getSendMessageResponse().getMessage()).build())
                .onErrorResume(Error.class, e -> Mono.just(Response.builder()
                        .code(e.getHttpsStatus()).description(((ErrorPush)e.getData()).getErrors().toString())
                        .build()))
                .onErrorResume(e -> Mono.just(Response.builder()
                            .code(STATUS_ERROR).description(e.getMessage())
                            .build()));
    }
}
