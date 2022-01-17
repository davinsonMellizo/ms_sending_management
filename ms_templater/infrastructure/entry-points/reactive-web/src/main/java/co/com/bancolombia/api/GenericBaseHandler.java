package co.com.bancolombia.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class GenericBaseHandler {

    protected Map<String, String> setHeaders(ServerRequest serverRequest) {
        return serverRequest.headers()
                .asHttpHeaders()
                .entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, v -> String.join(",", v.getValue())));
    }
}
