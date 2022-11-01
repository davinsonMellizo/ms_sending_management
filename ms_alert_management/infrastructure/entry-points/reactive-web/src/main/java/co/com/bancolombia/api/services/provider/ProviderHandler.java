package co.com.bancolombia.api.services.provider;

import co.com.bancolombia.api.commons.handlers.ValidatorHandler;
import co.com.bancolombia.api.commons.util.ParamsUtil;
import co.com.bancolombia.api.dto.ResponseDTO;
import co.com.bancolombia.api.dto.ProviderDTO;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.usecase.provider.ProviderUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.BODY_MISSING_ERROR;

@Component
@RequiredArgsConstructor
public class ProviderHandler {
    private final ProviderUseCase useCase;
    private final ValidatorHandler validatorHandler;

    public Mono<ServerResponse> findProviderById(ServerRequest serverRequest) {
        return ParamsUtil.getId(serverRequest)
                .flatMap(useCase::findProviderById)
                .flatMap(ResponseDTO::responseOk);
    }

    public Mono<ServerResponse> findAllProvider(ServerRequest serverRequest) {
        return useCase.findAllProviders()
                .flatMap(ResponseDTO::responseOk);
    }

    public Mono<ServerResponse> saveProvider(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(ProviderDTO.class)
                .switchIfEmpty(Mono.error(new TechnicalException(BODY_MISSING_ERROR)))
                .doOnNext(validatorHandler::validateObject)
                .flatMap(ProviderDTO::toModel)
                .flatMap(useCase::saveProvider)
                .flatMap(ResponseDTO::responseOk);
    }

    public Mono<ServerResponse> updateProvider(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(ProviderDTO.class)
                .switchIfEmpty(Mono.error(new TechnicalException(BODY_MISSING_ERROR)))
                .doOnNext(validatorHandler::validateObject)
                .flatMap(ProviderDTO::toModel)
                .flatMap(useCase::updateProvider)
                .flatMap(ResponseDTO::responseOk);
    }

    public Mono<ServerResponse> deleteProviderById(ServerRequest serverRequest) {
        return ParamsUtil.getId(serverRequest)
                .flatMap(useCase::deleteProviderById)
                .flatMap(ResponseDTO::responseOk);
    }

}
