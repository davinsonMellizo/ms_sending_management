package co.com.bancolombia.consumer;

import co.com.bancolombia.Request;
import co.com.bancolombia.consumer.adapter.response.Error;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
@RequiredArgsConstructor
public class RestClient<T extends Request, R> {

    private final WebClient webClient;
    @Autowired
    @Qualifier("INA")
    private final WebClient webClientConfigIna;
    private final Log LOGGER = LogFactory.getLog(RestClient.class);

    public <S> Mono<R> post(String route, T request, Class<R> clazz, Class<S> clazzError) {

        if (route.contains("masivapp.com")) {
            return webClient.post()
                    .uri(route)
                    .contentType(APPLICATION_JSON)
                    .headers(head -> head.setAll( request.getHeaders()))
                    .bodyValue(cleanHeader(request))
                    .retrieve()
                    .onStatus(HttpStatus::isError, response -> replyError(response, clazzError))
                    .bodyToMono(clazz);
        } else {
            return webClientConfigIna.post().uri(route)
                    .contentType(APPLICATION_JSON)
                    .headers(head -> head.setAll( request.getHeaders()))
                    .bodyValue(cleanHeader(request))
                    .retrieve()
                    .onStatus(HttpStatus::isError, response -> replyError(response, clazzError))
                    .bodyToMono(clazz);
        }
    }
    public <E,S> Mono<E> requestGet(String route, MultiValueMap<String, String> query, Map<String, String> body,
                                    Class<E> clsSuccess, Class<S> clsError){
        return WebClient.create(route)
                .method(HttpMethod.GET)
                .uri(uri -> uri.queryParams(query).build())
                .contentType(APPLICATION_JSON)
                .body(BodyInserters.fromValue(body))
                .retrieve()
                .onStatus(HttpStatus::isError, response -> replyError(response, clsError))
                .bodyToMono(clsSuccess);
    }

    private <S> Mono<Throwable> replyError(ClientResponse clientResponse, Class<S> clazzError) {
        return clientResponse.bodyToMono(clazzError)
                .map(data -> new Error(clientResponse.statusCode().value(), data));
    }

    private T cleanHeader(T request) {
        request.setHeaders(null);
        return request;
    }
}

