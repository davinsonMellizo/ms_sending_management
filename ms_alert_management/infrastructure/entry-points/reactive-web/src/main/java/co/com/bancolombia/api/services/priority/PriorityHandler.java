package co.com.bancolombia.api.services.priority;

import co.com.bancolombia.api.commons.handlers.ValidatorHandler;
import co.com.bancolombia.api.commons.util.ParamsUtil;
import co.com.bancolombia.api.dto.ResponseDTO;
import co.com.bancolombia.api.dto.PriorityDTO;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.usecase.priority.PriorityUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.BODY_MISSING_ERROR;

@Component
@RequiredArgsConstructor
public class PriorityHandler {
    private final PriorityUseCase useCase;
    private final ValidatorHandler validatorHandler;

    public Mono<ServerResponse> findPriorityById(ServerRequest serverRequest) {
        return ParamsUtil.getId(serverRequest)
                .map(Integer::parseInt)
                .flatMap(useCase::findPriorityById)
                .flatMap(ResponseDTO::responseOk);
    }

    public Mono<ServerResponse> findAllPriorityByProvider(ServerRequest serverRequest) {
        return ParamsUtil.getId(serverRequest)
                .flatMap(useCase::findAllByProvider)
                .flatMap(ResponseDTO::responseOk);
    }

    public Mono<ServerResponse> savePriority(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(PriorityDTO.class)
                .switchIfEmpty(Mono.error(new TechnicalException(BODY_MISSING_ERROR)))
                .doOnNext(validatorHandler::validateObject)
                .flatMap(PriorityDTO::toModel)
                .flatMap(useCase::savePriority)
                .flatMap(ResponseDTO::responseOk);
    }

    public Mono<ServerResponse> updatePriority(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(PriorityDTO.class)
                .switchIfEmpty(Mono.error(new TechnicalException(BODY_MISSING_ERROR)))
                .doOnNext(validatorHandler::validateObject)
                .flatMap(PriorityDTO::toModel)
                .flatMap(useCase::updatePriority)
                .flatMap(ResponseDTO::responseOk);
    }

    public Mono<ServerResponse> deletePriorityById(ServerRequest serverRequest) {
        return ParamsUtil.getId(serverRequest)
                .map(Integer::parseInt)
                .flatMap(useCase::deletePriorityById)
                .flatMap(ResponseDTO::responseOk);
    }

}
