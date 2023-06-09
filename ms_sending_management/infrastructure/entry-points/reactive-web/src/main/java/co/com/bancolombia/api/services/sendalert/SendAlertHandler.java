package co.com.bancolombia.api.services.sendalert;

import co.com.bancolombia.api.commons.handlers.ValidatorHandler;
import co.com.bancolombia.api.commons.util.ResponseUtil;
import co.com.bancolombia.api.dto.AlertDTO;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.usecase.sendalert.SendingUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.BODY_MISSING_ERROR;

@Component
@RequiredArgsConstructor
public class SendAlertHandler {
    private final SendingUseCase useCase;
    private final ValidatorHandler validatorHandler;

    public Mono<ServerResponse> sendAlertByClient(ServerRequest serverRequest) {

        return serverRequest.bodyToMono(AlertDTO.class)
                .switchIfEmpty(Mono.error(new TechnicalException(BODY_MISSING_ERROR)))
                .doOnNext(validatorHandler::validateObject)
                .flatMap(AlertDTO::toModel)
                .flatMap(useCase::sendAlertManager)
                .flatMap(ResponseUtil::responseOk);
    }

    public Mono<ServerResponse> sendAlertByContacts(ServerRequest serverRequest) {

        return serverRequest.bodyToMono(AlertDTO.class)
                .switchIfEmpty(Mono.error(new TechnicalException(BODY_MISSING_ERROR)))
                .doOnNext(validatorHandler::validateObject)
                .flatMap(AlertDTO::toModel)
                .flatMap(useCase::sendAlertManager)
                .flatMap(ResponseUtil::responseOk);
    }

}
