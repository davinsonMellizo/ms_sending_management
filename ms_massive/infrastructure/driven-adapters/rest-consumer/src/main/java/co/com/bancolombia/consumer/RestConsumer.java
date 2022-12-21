package co.com.bancolombia.consumer;

import co.com.bancolombia.consumer.model.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Component
@RequiredArgsConstructor
public class RestConsumer {

    private final WebClient client;

    public <S, E> Mono<S> get(String route, MultiValueMap<String, String> queryParams, Class<S> success, Class<E> error) {
        return client.get()
                .uri(uriBuilder -> uriBuilder.path(route).queryParams(queryParams).build())
                .retrieve()
                .onStatus(HttpStatus::isError, res -> buildError(res, error))
                .bodyToMono(success);
    }

    private <S> Mono<Throwable> buildError(ClientResponse clientResponse, Class<S> error) {
        return clientResponse.bodyToMono(error)
                .map(d -> Error.builder()
                        .status(clientResponse.statusCode().value())
                        .data(d)
                        .build());
    }

}