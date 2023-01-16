package co.com.bancolombia.api.services.event;

import co.com.bancolombia.api.commons.util.ResponseUtil;
import co.com.bancolombia.log.LoggerBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class EventHandler {
    private final LoggerBuilder loggerBuilder;
    public Mono<ServerResponse> eventRegister(ServerRequest serverRequest) {
        String message = "Registered Event OK";
        return serverRequest.bodyToMono(String.class)
                .doOnNext(s -> loggerBuilder.info(s))
                .map(s -> message)
                .flatMap(ResponseUtil::responseOk);
    }

}
