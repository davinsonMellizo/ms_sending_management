package co.com.bancolombia.consumer;

import co.com.bancolombia.Request;
import co.com.bancolombia.consumer.adapter.response.Error;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
@RequiredArgsConstructor
public class RestClient<T extends Request,R> {

    private final WebClient webClient;
    @Autowired
    @Qualifier("INA")
    private final WebClient webClientConfigIna;
    private final Log LOGGER= LogFactory.getLog(RestClient.class);

    public <S> Mono<R> post(String route, T request, Class<R> clazz, Class<S> clazzError) {
        if (route.contains("masivapp.com")) {
            return webClient.post()
                    .uri(route)
                    .contentType(APPLICATION_JSON)
                    .headers(head -> head.setAll(request.getHeaders()))
                    .bodyValue(cleanHeader(request))
                    .retrieve()
                    .onStatus(HttpStatus::isError, response -> replyError(response, clazzError))
                    .bodyToMono(clazz);
        } else {
            return webClientConfigIna.post().uri(route)
                    .contentType(APPLICATION_JSON)
                    .headers(head -> head.setAll(request.getHeaders()))
                    .bodyValue(cleanHeader(request))
                    .retrieve()
                    .onStatus(HttpStatus::isError, response -> replyError(response, clazzError))
                    .bodyToMono(clazz);
        }
    }

    private <S> Mono<Throwable> replyError(ClientResponse clientResponse, Class<S> clazzError){
        return clientResponse.bodyToMono(clazzError)
                .map(data -> new Error(clientResponse.statusCode().value(), data));
}

    private <T extends Request> T cleanHeader(T request) {
        request.setHeaders(null);
        return request;
    }
}

