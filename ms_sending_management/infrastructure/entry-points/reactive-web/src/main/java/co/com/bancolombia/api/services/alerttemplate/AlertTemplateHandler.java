package co.com.bancolombia.api.services.alerttemplate;

import co.com.bancolombia.api.commons.handlers.ValidatorHandler;
import co.com.bancolombia.api.commons.util.ParamsUtil;
import co.com.bancolombia.api.commons.util.ResponseUtil;
import co.com.bancolombia.api.dto.AlertTemplateDTO;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.usecase.alerttemplate.AlertTemplateUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.BODY_MISSING_ERROR;

@Component
@RequiredArgsConstructor
public class AlertTemplateHandler {

    private final AlertTemplateUseCase useCase;
    private final ValidatorHandler validatorHandler;

    public Mono<ServerResponse> saveAlertTemplate(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(AlertTemplateDTO.class)
                .switchIfEmpty(Mono.error(new TechnicalException(BODY_MISSING_ERROR)))
                .doOnNext(validatorHandler::validateObject)
                .flatMap(AlertTemplateDTO::toModel)
                .flatMap(useCase::saveAlertTemplate)
                .flatMap(ResponseUtil::responseOk);
    }

    public Mono<ServerResponse> findAlertTemplate(ServerRequest serverRequest) {
        return ParamsUtil.getId(serverRequest)
                .map(Integer::parseInt)
                .flatMap(useCase::findAlertTemplateById)
                .flatMap(ResponseUtil::responseOk);
    }

    public Mono<ServerResponse> deleteAlertTemplate(ServerRequest serverRequest) {
        return ParamsUtil.getId(serverRequest)
                .map(Integer::parseInt)
                .flatMap(useCase::deleteAlertTemplateById)
                .flatMap(ResponseUtil::responseOk);
    }

}
