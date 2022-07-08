package co.com.bancolombia.consumer;

import co.com.bancolombia.consumer.adapter.Error;
import co.com.bancolombia.log.LoggerBuilder;
import co.com.bancolombia.model.Request;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;


@Component
@RequiredArgsConstructor
public class RestConsumer<T extends Request,R> {

    private final WebClient webClient;
    private final ObjectMapper  mapper = new ObjectMapper();
    private final LoggerBuilder loggerBuilder;

    public <S> Mono<R> post(String route, T request, Class<R> clazz, Class<S> clazzError) {
        Map<String,String> header = request.getHeaders();
        return webClient.post()
                .uri(route)
                .headers(head -> head.setAll(header))
                .bodyValue(cleanHeader(request))
                .retrieve()
                .onStatus(HttpStatus::isError, response -> replyError(response, clazzError))
                .bodyToMono(clazz);
    }

    private String cleanHeader(T request){
        request.setHeaders(null);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String data = "";
        try {
            data = mapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            loggerBuilder.error(e);
        }
        return data;
    }

    private <S> Mono<Throwable> replyError(ClientResponse clientResponse, Class<S> clazzError){
        return clientResponse.bodyToMono(clazzError)
                .map(data -> new Error(clientResponse.statusCode().value(), data));
    }


}
