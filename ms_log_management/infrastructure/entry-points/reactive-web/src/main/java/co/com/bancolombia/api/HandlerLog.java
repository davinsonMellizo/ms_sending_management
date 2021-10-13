package co.com.bancolombia.api;

import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.usecase.log.LogUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.HEADER_MISSING_ERROR;

@Component
@RequiredArgsConstructor
public class HandlerLog {
    private  final LogUseCase useCase;
    public Mono<ServerResponse> listLogs(ServerRequest serverRequest) {
        return Mono.just(ParamsUtil.setHeaders(serverRequest))
                .filter(headers -> !headers.isEmpty())
                .switchIfEmpty(Mono.error(new TechnicalException(HEADER_MISSING_ERROR)))
                .flatMap(useCase::listLogs)
                .flatMap(ResponseUtil::responseOk);
    }
}
