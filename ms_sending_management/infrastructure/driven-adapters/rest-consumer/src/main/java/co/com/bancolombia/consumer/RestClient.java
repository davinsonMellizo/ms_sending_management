package co.com.bancolombia.consumer;

import co.com.bancolombia.Request;
import co.com.bancolombia.consumer.adapter.response.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
@RequiredArgsConstructor
public class RestClient<T extends Request,R> {

    private final WebClient webClient;

    public <S extends Error> Mono<R> post(String route, T request, Class<R> clazz, Class<S> clazzError) {
        Map<String,String> header = request.getHeaders();
        return webClient.post()
                .uri(route)
                .contentType(APPLICATION_JSON)
                //.headers(head -> head.setAll(header))
                .bodyValue(cleanHeader(request))
                .retrieve()
                .onStatus(HttpStatus::isError, response -> replyError(response, clazzError))
                .bodyToMono(clazz);
    }

    public Mono<R> get(String route, T request, Class<R> clazz) {
        Map<String,String> header = request.getHeaders();
        return webClient.get()
                .uri(route)
                .headers(head -> head.setAll(header))
                .retrieve()
                .bodyToMono(clazz);
    }

    private <T extends Request> T cleanHeader(T request){
        request.setHeaders(null);
        return request;
    }

    private <S extends Error> Mono<Throwable> replyError(ClientResponse clientResponse, Class<S> clazzError){
        return clientResponse.bodyToMono(clazzError)
                .map(error -> new Error(clientResponse.statusCode().value()));
    }

}
