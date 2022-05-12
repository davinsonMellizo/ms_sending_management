package co.com.bancolombia.api.services.clientmacd;

import co.com.bancolombia.api.commons.handlers.ValidatorHandler;
import co.com.bancolombia.api.commons.util.ResponseUtil;
import co.com.bancolombia.api.dto.EnrolDTO;
import co.com.bancolombia.api.mapper.EnrolMapper;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.usecase.client.ClientUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.BODY_MISSING_ERROR;

@Component
@RequiredArgsConstructor
public class ClientMcdHandler {
    private final ClientUseCase clientUseCase;
    private final ValidatorHandler validatorHandler;
    private final EnrolMapper enrolMapper;

    public Mono<ServerResponse> updateClientMcd(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(EnrolDTO.class)
                .switchIfEmpty(Mono.error(new TechnicalException(BODY_MISSING_ERROR)))
                .doOnNext(validatorHandler::validateObject)
                .map(enrolMapper::toEntity)
                .flatMap(clientUseCase::updateClientMcd)
                .flatMap(ResponseUtil::responseOk);
    }

}
