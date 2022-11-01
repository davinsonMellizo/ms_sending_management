package co.com.bancolombia.api.services.alerttransaction;

import co.com.bancolombia.api.commons.handlers.ValidatorHandler;
import co.com.bancolombia.api.commons.util.ParamsUtil;
import co.com.bancolombia.api.dto.ResponseDTO;
import co.com.bancolombia.api.dto.AlertTransactionDTO;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.usecase.alerttransaction.AlertTransactionUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.BODY_MISSING_ERROR;

@Component
@RequiredArgsConstructor
public class AlertTransactionHandler {
    private final AlertTransactionUseCase useCase;
    private final ValidatorHandler validatorHandler;

    public Mono<ServerResponse> findAllAlertTransaction(ServerRequest serverRequest) {
        return ParamsUtil.getId(serverRequest)
                .flatMap(useCase::findAllAlertTransaction)
                .flatMap(ResponseDTO::responseOk);
    }

    public Mono<ServerResponse> saveAlertTransaction(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(AlertTransactionDTO.class)
                .switchIfEmpty(Mono.error(new TechnicalException(BODY_MISSING_ERROR)))
                .doOnNext(validatorHandler::validateObject)
                .flatMap(AlertTransactionDTO::toModel)
                .flatMap(useCase::saveAlertTransaction)
                .flatMap(ResponseDTO::responseOk);
    }

    public Mono<ServerResponse> deleteAlertTransaction(ServerRequest serverRequest) {
        return ParamsUtil.getRelationAlert(serverRequest)
                .doOnNext(validatorHandler::validateObjectHeaders)
                .flatMap(AlertTransactionDTO::toModel)
                .flatMap(useCase::deleteAlertTransaction)
                .flatMap(ResponseDTO::responseOk);
    }

}
