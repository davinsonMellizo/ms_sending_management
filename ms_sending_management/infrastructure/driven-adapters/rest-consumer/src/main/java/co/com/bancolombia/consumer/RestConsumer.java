package co.com.bancolombia.consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Service
@RequiredArgsConstructor
public class RestConsumer<T,R>{

    private final WebClient client;

    public Mono<R> get(String uri, Class<R> clazz) {
        return client
                .get()
                .uri(uri)
                .retrieve()
                .bodyToMono(clazz);

    }
}