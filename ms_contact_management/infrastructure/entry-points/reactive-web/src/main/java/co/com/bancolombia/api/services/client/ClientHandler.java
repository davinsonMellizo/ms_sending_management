package co.com.bancolombia.api.services.client;

import co.com.bancolombia.api.commons.handlers.ValidatorHandler;
import co.com.bancolombia.api.commons.util.ParamsUtil;
import co.com.bancolombia.api.commons.util.ResponseUtil;
import co.com.bancolombia.api.dto.ClientDTO;
import co.com.bancolombia.api.header.ClientHeader;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.usecase.client.ClientUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.BODY_MISSING_ERROR;
import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.HEADERS_MISSING_ERROR;

@Component
@RequiredArgsConstructor
public class ClientHandler {
    private final ClientUseCase clientUseCase;
    private final ValidatorHandler validatorHandler;

    public Mono<ServerResponse> findContact(ServerRequest serverRequest) {
        return ParamsUtil.getClientHeaders(serverRequest)
                .switchIfEmpty(Mono.error(new TechnicalException(HEADERS_MISSING_ERROR)))
                .doOnNext(validatorHandler::validateObjectHeaders)
                .flatMap(ClientHeader::toModel)
                .flatMap(clientUseCase::findClientByIdentification)
                .flatMap(ResponseUtil::responseOk);
    }

    public Mono<ServerResponse> saveContact(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(ClientDTO.class)
                .switchIfEmpty(Mono.error(new TechnicalException(BODY_MISSING_ERROR)))
                .doOnNext(validatorHandler::validateObject)
                .flatMap(ClientDTO::toModel)
                .flatMap(clientUseCase::saveClient)
                .flatMap(ResponseUtil::responseOk);
    }

    public Mono<ServerResponse> updateContact(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(ClientDTO.class)
                .switchIfEmpty(Mono.error(new TechnicalException(BODY_MISSING_ERROR)))
                .doOnNext(validatorHandler::validateObject)
                .flatMap(ClientDTO::toModel)
                .flatMap(clientUseCase::updateClient)
                .flatMap(ResponseUtil::responseOk);
    }

    public Mono<ServerResponse> deleteContact(ServerRequest serverRequest) {
        return ParamsUtil.getClientHeaders(serverRequest)
                .switchIfEmpty(Mono.error(new TechnicalException(HEADERS_MISSING_ERROR)))
                .doOnNext(validatorHandler::validateObjectHeaders)
                .flatMap(ClientHeader::toModel)
                .flatMap(clientUseCase::deleteClient)
                .flatMap(ResponseUtil::responseOk);
    }

}
