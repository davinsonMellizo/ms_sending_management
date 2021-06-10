package co.com.bancolombia.api.services.alert;

import co.com.bancolombia.api.commons.handlers.ValidatorHandler;
import co.com.bancolombia.api.commons.util.ParamsUtil;
import co.com.bancolombia.api.commons.util.ResponseUtil;
import co.com.bancolombia.api.dto.AlertDTO;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.usecase.alert.AlertUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.BODY_MISSING_ERROR;
import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.QUERY_MISSING_ERROR;

@Component
@RequiredArgsConstructor
public class AlertHandler {
    private final AlertUseCase alertUseCase;
    private final ValidatorHandler validatorHandler;

    public Mono<ServerResponse> findAlert(ServerRequest serverRequest) {
        return ParamsUtil.getIdAlert(serverRequest)
                .switchIfEmpty(Mono.error(new TechnicalException(QUERY_MISSING_ERROR)))
                .flatMap(alertUseCase::findAlertByIdRequest)
                .flatMap(ResponseUtil::responseOk);
    }

    public Mono<ServerResponse> saveAlert(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(AlertDTO.class)
                .switchIfEmpty(Mono.error(new TechnicalException(BODY_MISSING_ERROR)))
                .doOnNext(validatorHandler::validateObject)
                .flatMap(AlertDTO::toModel)
                .flatMap(alertUseCase::saveAlertRequest)
                .flatMap(ResponseUtil::responseOk);
    }

    public Mono<ServerResponse> updateAlert(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(AlertDTO.class)
                .switchIfEmpty(Mono.error(new TechnicalException(BODY_MISSING_ERROR)))
                .doOnNext(validatorHandler::validateObject)
                .flatMap(AlertDTO::toModel)
                .flatMap(alertUseCase::updateAlertRequest)
                .flatMap(ResponseUtil::responseOk);
    }

    public Mono<ServerResponse> deleteAlert(ServerRequest serverRequest) {
        return ParamsUtil.getIdAlert(serverRequest)
                .switchIfEmpty(Mono.error(new TechnicalException(QUERY_MISSING_ERROR)))
                .flatMap(alertUseCase::deleteAlertRequest)
                .flatMap(ResponseUtil::responseOk);
    }

}
