package co.com.bancolombia.consumer;

import co.com.bancolombia.Request;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;


@Component
@RequiredArgsConstructor
public class RestClient<T extends Request,R> {

    private final WebClient webClient;

    public Mono<R> post(String route, T request, Class<R> clazz) {
        Map<String,String> header = request.getHeaders();
        return webClient.post()
                .uri(route)
                .headers(head -> head.setAll(header))
                .bodyValue(cleanHeader(request))
                .retrieve()
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
}
