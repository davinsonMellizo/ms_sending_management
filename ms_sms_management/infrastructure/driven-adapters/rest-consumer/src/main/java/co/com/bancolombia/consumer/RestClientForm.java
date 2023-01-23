package co.com.bancolombia.consumer;

import co.com.bancolombia.consumer.adapter.response.Error;

import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;

@Component
@RequiredArgsConstructor

public class RestClientForm<T,R> {

    private final WebClient webClient;
    private final Log LOGGER= LogFactory.getLog(RestClientForm.class);


    public <S> Mono<R> post(String route, MultiValueMap formData, Class<R> clazz, Class<S> clazzError) {
            return webClient.post()
                    .uri(route)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .contentType(APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData(formData))
                    .retrieve()
                    .onStatus(HttpStatus::isError, response -> replyError(response, clazzError))
                    .bodyToMono(clazz);
    }

    private <S> Mono<Throwable> replyError(ClientResponse clientResponse, Class<S> clazzError){
        return clientResponse.bodyToMono(clazzError)
                .map(data -> new Error(clientResponse.statusCode().value(), data));
    }
}

