package co.com.bancolombia.api.commons.util;

import lombok.experimental.UtilityClass;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

@UtilityClass
public class ParamsUtil {

    public static final String ID = "id";
    public static final String CODE = "code";

    private static Mono<String> ofEmpty(String value) {
        return (value == null || value.isEmpty()) ? Mono.empty() : Mono.just(value);
    }

    public static Mono<String> getId(ServerRequest request) {
        return ofEmpty(request.pathVariable(ID));
    }

    public static Mono<String> getCode(ServerRequest request) {
        return ofEmpty(request.pathVariable(CODE));
    }


}