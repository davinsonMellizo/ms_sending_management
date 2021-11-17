package co.com.bancolombia.consumer.adapter;

import co.com.bancolombia.consumer.RestClient;
import co.com.bancolombia.consumer.adapter.response.*;
import co.com.bancolombia.consumer.config.ConsumerProperties;
import co.com.bancolombia.model.message.PUSH;
import co.com.bancolombia.model.message.Response;
import co.com.bancolombia.model.message.gateways.PushGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class PushAdapter implements PushGateway {

    private static Integer STATUS_OK = 202;
    private static Integer STATUS_ERROR = 1;
    private final ConsumerProperties properties;
    private final RestClient<PUSH, SuccessPush> client;

    @Override
    public Mono<Response> sendPush(PUSH push) {
        return client.post(properties.getResources().getEndpointPush(), push,
                SuccessPush.class, ErrorPush.class)
                .map(response -> Response.builder().code(STATUS_OK)
                        .description(response.getData().getSendMessageResponse().getMessage()).build())
                .onErrorResume(ErrorPush.class, e -> Mono.just(Response.builder()
                        .code(e.getHttpsStatus()).description(e.toString())
                        .build()))
                .onErrorResume(WebClientException.class, e -> Mono.just(Response.builder()
                        .code(STATUS_ERROR).description(e.getMessage())
                        .build()));
    }
}
