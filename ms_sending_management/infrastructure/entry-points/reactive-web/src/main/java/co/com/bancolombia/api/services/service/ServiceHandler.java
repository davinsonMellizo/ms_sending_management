package co.com.bancolombia.api.services.service;

import co.com.bancolombia.api.commons.handlers.ValidatorHandler;
import co.com.bancolombia.api.commons.util.ParamsUtil;
import co.com.bancolombia.api.commons.util.ResponseUtil;
import co.com.bancolombia.api.dto.ServiceDTO;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.usecase.service.ServiceUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.BODY_MISSING_ERROR;

@Component
@RequiredArgsConstructor
public class ServiceHandler {
    private final ServiceUseCase useCase;
    private final ValidatorHandler validatorHandler;

    public Mono<ServerResponse> findService(ServerRequest serverRequest) {
        return ParamsUtil.getId(serverRequest)
                .map(Integer::parseInt)
                .flatMap(useCase::findServiceById)
                .flatMap(ResponseUtil::responseOk);
    }

    public Mono<ServerResponse> saveService(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(ServiceDTO.class)
                .switchIfEmpty(Mono.error(new TechnicalException(BODY_MISSING_ERROR)))
                .doOnNext(validatorHandler::validateObject)
                .flatMap(ServiceDTO::toModel)
                .flatMap(useCase::saveService)
                .flatMap(ResponseUtil::responseOk);
    }

    public Mono<ServerResponse> updateService(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(ServiceDTO.class)
                .switchIfEmpty(Mono.error(new TechnicalException(BODY_MISSING_ERROR)))
                .doOnNext(validatorHandler::validateObject)
                .flatMap(ServiceDTO::toModel)
                .flatMap(useCase::updateService)
                .flatMap(ResponseUtil::responseOk);
    }

    public Mono<ServerResponse> deleteService(ServerRequest serverRequest) {
        return ParamsUtil.getId(serverRequest)
                .map(Integer::parseInt)
                .flatMap(useCase::deleteServiceById)
                .flatMap(ResponseUtil::responseOk);
    }

}
