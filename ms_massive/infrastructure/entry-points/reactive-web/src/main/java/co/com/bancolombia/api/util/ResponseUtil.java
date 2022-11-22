package co.com.bancolombia.api.util;

import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@UtilityClass
public class ResponseUtil {

    public static <T> Mono<ServerResponse> responseOk(T response) {
        return buildResponse(HttpStatus.OK, response);
    }

    public static <T> Mono<ServerResponse> responseFail(T body) {
        return buildResponse(INTERNAL_SERVER_ERROR, body);
    }

    public static <T> Mono<ServerResponse> buildResponse(HttpStatus status, T body) {
        return ServerResponse
                .status(status)
                .contentType(APPLICATION_JSON)
                .bodyValue(body);
    }
}
