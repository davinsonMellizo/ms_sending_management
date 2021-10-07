package co.com.bancolombia.api.services.alertclient;

import co.com.bancolombia.api.commons.handlers.ValidatorHandler;
import co.com.bancolombia.api.commons.util.ParamsUtil;
import co.com.bancolombia.api.commons.util.ResponseUtil;
import co.com.bancolombia.api.dto.AlertClientDTO;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.usecase.alertclient.AlertClientUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.stream.Collectors;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.BODY_MISSING_ERROR;
import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.HEADER_MISSING_ERROR;

@Component
@RequiredArgsConstructor
public class AlertClientHandler {
    private final AlertClientUseCase useCase;
    private final ValidatorHandler validatorHandler;

    public Mono<ServerResponse> findAllAlertClient(ServerRequest serverRequest) {
        return ParamsUtil.getId(serverRequest)
                .map(Integer::parseInt)
                .flatMap(useCase::findAllAlertClient)
                .flatMap(ResponseUtil::responseOk);
    }

    public Mono<ServerResponse> updateAlertClient(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(AlertClientDTO.class)
                .switchIfEmpty(Mono.error(new TechnicalException(BODY_MISSING_ERROR)))
                .doOnNext(validatorHandler::validateObject)
                .flatMap(AlertClientDTO::toModel)
                .flatMap(useCase::updateAlertClient)
                .flatMap(ResponseUtil::responseOk);
    }

    public Mono<ServerResponse> saveAlertClient(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(AlertClientDTO.class)
                .switchIfEmpty(Mono.error(new TechnicalException(BODY_MISSING_ERROR)))
                .doOnNext(validatorHandler::validateObject)
                .flatMap(AlertClientDTO::toModel)
                .flatMap(useCase::saveAlertClient)
                .flatMap(ResponseUtil::responseOk);
    }

    public Mono<ServerResponse> basicKit(ServerRequest serverRequest) {
          return ParamsUtil.validateHeader(serverRequest)
                  .switchIfEmpty(Mono.error(new TechnicalException(HEADER_MISSING_ERROR)))
                  .flatMap(useCase::matchClientWithBasicKit)
                  .flatMap(ResponseUtil::responseOk);
    }

    public Mono<ServerResponse> deleteAlertClient(ServerRequest serverRequest) {
        return ParamsUtil.getRelationClient(serverRequest)
                .flatMap(AlertClientDTO::toModel)
                .flatMap(useCase::deleteAlertClient)
                .flatMap(ResponseUtil::responseOk);
    }
}