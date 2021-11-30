package co.com.bancolombia.api.services.sendalert;

import co.com.bancolombia.api.commons.handlers.ValidatorHandler;
import co.com.bancolombia.api.commons.util.ResponseUtil;
import co.com.bancolombia.api.services.sendalert.dto.MessageDTO;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.model.message.Message;
import co.com.bancolombia.usecase.sendalert.ManagementAlertUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.BODY_MISSING_ERROR;

@Component
@RequiredArgsConstructor
public class SendAlertHandler {
    private final ManagementAlertUseCase useCase;
    private final ValidatorHandler validatorHandler;


    public Mono<ServerResponse> saveRemitter(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(MessageDTO.class)
                .switchIfEmpty(Mono.error(new TechnicalException(BODY_MISSING_ERROR)))
                .flatMap(MessageDTO::toModel)
                .flatMap(useCase::alertSendingManager)
                .flatMap(ResponseUtil::responseOk);
    }

}
