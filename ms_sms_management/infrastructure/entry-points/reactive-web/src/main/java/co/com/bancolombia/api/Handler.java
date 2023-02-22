package co.com.bancolombia.api;

import co.com.bancolombia.api.DTO.AlertDTO;
import co.com.bancolombia.api.commons.util.ResponseUtil;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.usecase.sendalert.SendAlertUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.BODY_MISSING_ERROR;

@Component
@RequiredArgsConstructor
public class Handler {
    private final SendAlertUseCase useCase;
    public Mono<ServerResponse> sendSms(ServerRequest serverRequest) {

        return serverRequest.bodyToMono(AlertDTO.class)
                .switchIfEmpty(Mono.error(new TechnicalException(BODY_MISSING_ERROR)))
                .flatMap(AlertDTO::toModel)
                .flatMap(useCase::sendAlert)
                .flatMap(ResponseUtil::responseOk);
    }
}
