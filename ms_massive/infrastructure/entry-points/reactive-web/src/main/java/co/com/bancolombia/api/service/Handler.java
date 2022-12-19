package co.com.bancolombia.api.service;

import co.com.bancolombia.api.dto.MassiveDTO;
import co.com.bancolombia.api.handlers.ValidatorHandler;
import co.com.bancolombia.api.util.ResponseUtil;
import co.com.bancolombia.model.commons.exception.TechnicalException;
import co.com.bancolombia.usecase.MassiveUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.model.commons.enums.TechnicalExceptionEnum.TECHNICAL_MISSING_PARAMETERS;

@Component
@RequiredArgsConstructor
public class Handler {

    private final ValidatorHandler validatorHandler;
    private final MassiveUseCase useCase;

    public Mono<ServerResponse> send(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(MassiveDTO.class)
                .switchIfEmpty(Mono.error(new TechnicalException(TECHNICAL_MISSING_PARAMETERS)))
                .doOnNext(validatorHandler::validateObject)
                .flatMap(MassiveDTO::toModel)
                .flatMap(useCase::sendCampaign)
                .flatMap(ResponseUtil::responseOk);
    }
}
