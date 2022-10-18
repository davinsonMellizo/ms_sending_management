package co.com.bancolombia.api.services.remitter;

import co.com.bancolombia.api.commons.handlers.ValidatorHandler;
import co.com.bancolombia.api.commons.util.ParamsUtil;
import co.com.bancolombia.api.dto.ResponseDTO;
import co.com.bancolombia.api.dto.RemitterDTO;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.usecase.remitter.RemitterUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.BODY_MISSING_ERROR;

@Component
@RequiredArgsConstructor
public class RemitterHandler {
    private final RemitterUseCase useCase;
    private final ValidatorHandler validatorHandler;

    public Mono<ServerResponse> findRemitter(ServerRequest serverRequest) {
        return ParamsUtil.getId(serverRequest)
                .map(Integer::parseInt)
                .flatMap(useCase::findRemitterById)
                .flatMap(ResponseDTO::responseOk);
    }

    public Mono<ServerResponse> findAllRemitter(ServerRequest serverRequest) {
        return useCase.findAllRemitter()
                .flatMap(ResponseDTO::responseOk);
    }

    public Mono<ServerResponse> saveRemitter(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(RemitterDTO.class)
                .switchIfEmpty(Mono.error(new TechnicalException(BODY_MISSING_ERROR)))
                .doOnNext(validatorHandler::validateObject)
                .flatMap(RemitterDTO::toModel)
                .flatMap(useCase::saveRemitter)
                .flatMap(ResponseDTO::responseOk);
    }

    public Mono<ServerResponse> updateRemitter(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(RemitterDTO.class)
                .switchIfEmpty(Mono.error(new TechnicalException(BODY_MISSING_ERROR)))
                .doOnNext(validatorHandler::validateObject)
                .flatMap(RemitterDTO::toModel)
                .flatMap(useCase::updateRemitter)
                .flatMap(ResponseDTO::responseOk);
    }

    public Mono<ServerResponse> deleteRemitter(ServerRequest serverRequest) {
        return ParamsUtil.getId(serverRequest)
                .map(Integer::parseInt)
                .flatMap(useCase::deleteRemitterById)
                .flatMap(ResponseDTO::responseOk);
    }

}
