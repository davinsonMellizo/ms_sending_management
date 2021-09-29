package co.com.bancolombia.api.services.providerservice;

import co.com.bancolombia.api.commons.handlers.ValidatorHandler;
import co.com.bancolombia.api.commons.util.ParamsUtil;
import co.com.bancolombia.api.commons.util.ResponseUtil;
import co.com.bancolombia.api.dto.AlertTransactionDTO;
import co.com.bancolombia.api.dto.ProviderServiceDTO;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.usecase.alerttransaction.AlertTransactionUseCase;
import co.com.bancolombia.usecase.providerservice.ProviderServiceUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.BODY_MISSING_ERROR;

@Component
@RequiredArgsConstructor
public class ProviderServiceHandler {
    private final ProviderServiceUseCase useCase;
    private final ValidatorHandler validatorHandler;

    public Mono<ServerResponse> findAllProviderService(ServerRequest serverRequest) {
        return useCase.findAllProviderService()
                .flatMap(ResponseUtil::responseOk);
    }

    public Mono<ServerResponse> saveProviderService(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(ProviderServiceDTO.class)
                .switchIfEmpty(Mono.error(new TechnicalException(BODY_MISSING_ERROR)))
                .doOnNext(validatorHandler::validateObject)
                .flatMap(ProviderServiceDTO::toModel)
                .flatMap(useCase::saveProviderService)
                .flatMap(ResponseUtil::responseOk);
    }

    public Mono<ServerResponse> deleteProviderService(ServerRequest serverRequest) {
        return ParamsUtil.getRelationProvider(serverRequest)
                .doOnNext(validatorHandler::validateObjectHeaders)
                .flatMap(ProviderServiceDTO::toModel)
                .flatMap(useCase::deleteProviderService)
                .flatMap(ResponseUtil::responseOk);
    }

}
