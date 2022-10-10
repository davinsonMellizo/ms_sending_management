package co.com.bancolombia.api.services.log;

import co.com.bancolombia.api.commons.util.ParamsUtil;
import co.com.bancolombia.api.commons.util.ResponseUtil;
import co.com.bancolombia.api.services.dto.QueryLogDto;
import co.com.bancolombia.usecase.log.LogUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;


@Component
@RequiredArgsConstructor
public class HandlerLog {
    private  final LogUseCase useCase;

    public Mono<ServerResponse> findLogsByDate(ServerRequest serverRequest, Integer daysHotDate) {
        return ParamsUtil.getClientHeaders(serverRequest, daysHotDate)
                .flatMap(QueryLogDto::toModel)
                .flatMap(useCase::findLogsByDate)
                .flatMap(ResponseUtil::responseOk);
    }

}
