package co.com.bancolombia.consumer.adapter;


import co.com.bancolombia.consumer.RestClient;
import co.com.bancolombia.consumer.config.ConsumerProperties;
import co.com.bancolombia.model.Request;
import co.com.bancolombia.model.client.ResponseClient;
import co.com.bancolombia.model.message.gateways.MessageGateway;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class MessageAdapter implements MessageGateway {

    private final ConsumerProperties properties;
    private final ObjectMapper objectMapper;
    private final RestClient<Request, ResponseClient> restClient;

    @Override
    public Mono<Boolean> sendAlert(Request request) {
        return restClient.post(properties.getResources().getSendAlert(), request, ResponseClient.class)
                .map(ResponseClient::getResponse);
    }

}