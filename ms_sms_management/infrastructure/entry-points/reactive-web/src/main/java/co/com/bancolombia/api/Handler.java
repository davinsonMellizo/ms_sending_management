package co.com.bancolombia.api;

import co.com.bancolombia.api.commons.util.ResponseUtil;
import co.com.bancolombia.api.dto.AlertDTO;
import co.com.bancolombia.api.commons.handlers.ValidatorHandler;
import co.com.bancolombia.api.dto.ResponseDTO;
import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.model.message.Alert;
import co.com.bancolombia.model.message.Response;
import co.com.bancolombia.usecase.log.LogUseCase;
import co.com.bancolombia.usecase.sendalert.SendAlertUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.BODY_MISSING_ERROR;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.TRANSACTION_OK;

@Component
@RequiredArgsConstructor
public class Handler {
    private final SendAlertUseCase useCase;
    private final LogUseCase logUseCase;
    private final ValidatorHandler validatorHandler;
    private final int RESPONSE_OK = 250;
    public Mono<ServerResponse> sendSms(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(AlertDTO.class)
                .switchIfEmpty(Mono.error(new TechnicalException(BODY_MISSING_ERROR)))
                .doOnNext(validatorHandler::validateObject)
                .flatMap(AlertDTO::toModel)
                .flatMap(this::sendAlert);
    }

    public Mono<ServerResponse> sendAlert(Alert alert) {
        return Mono.just(alert)
                .flatMap(useCase::sendAlert)
                .flatMap(response -> ResponseUtil.responseOk(ResponseDTO.builder()
                                .statusCode(TRANSACTION_OK.getCode())
                                .description(TRANSACTION_OK.getMessage())
                                .build()))
                .onErrorResume(BusinessException.class, e -> handleExceptions(e, alert))
                .onErrorResume(TechnicalException.class, e -> handleExceptions(e, alert))
                .onErrorResume(e ->  !(e instanceof TechnicalException) && !(e instanceof BusinessException),
                        e -> handleExceptions(e, alert))
                ;

    }
    private Mono<ServerResponse> handleExceptions(BusinessException businessException, Alert pAlert) {
        return  logUseCase.handlerLog(pAlert, "SMS", Response.builder().code(1)
                        .description(businessException.getMessage()).build(), true)
                .then(Mono.error(businessException));
    }

    private Mono<ServerResponse> handleExceptions(TechnicalException technicalException, Alert pAlert) {
        return logUseCase.handlerLog(pAlert, "SMS", Response.builder().code(technicalException.getCode())
                        .description(technicalException.getMessage()).build(), true)
                .then(Mono.error(technicalException));
    }

    private Mono<ServerResponse> handleExceptions(Throwable throwable, Alert pAlert) {
        return logUseCase.handlerLog(pAlert, "SMS", Response.builder().code(1)
                        .description(throwable.getMessage()).build(), true)
                .then(Mono.error(throwable));
    }
}
