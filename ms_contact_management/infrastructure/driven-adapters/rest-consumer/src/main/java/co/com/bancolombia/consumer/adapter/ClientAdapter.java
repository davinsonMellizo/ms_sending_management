package co.com.bancolombia.consumer.adapter;


import co.com.bancolombia.consumer.RestClient;
import co.com.bancolombia.consumer.config.ConsumerProperties;
import co.com.bancolombia.model.Request;
import co.com.bancolombia.model.client.Client;
import co.com.bancolombia.model.client.ResponseClient;
import co.com.bancolombia.model.client.gateways.ClientGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.HashMap;

import static co.com.bancolombia.commons.enums.Header.*;

@Component
@RequiredArgsConstructor
public class ClientAdapter implements ClientGateway {

    private final ConsumerProperties properties;
    private final RestClient<Request, ResponseClient> restClient;

    @Override
    public Mono<Boolean> matchClientWithBasicKit(Client client) {
        HashMap headers = new HashMap();
        headers.put(DOCUMENT_TYPE, client.getDocumentType());
        headers.put(DOCUMENT_NUMBER, Long.toString(client.getDocumentNumber()));
        headers.put(ASSOCIATION_ORIGIN, client.getEnrollmentOrigin());

        return Mono.just(Request.builder().headers(headers).build())
                .flatMap(clientRequest -> restClient.post(properties.getResources().getBasicKit(),
                        clientRequest, ResponseClient.class))
                .map(ResponseClient::getResponse);
    }
}