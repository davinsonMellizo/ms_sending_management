package co.com.bancolombia.api.services.client;

import co.com.bancolombia.api.commons.handlers.ValidatorHandler;
import co.com.bancolombia.api.commons.util.ParamsUtil;
import co.com.bancolombia.api.commons.util.ResponseUtil;
import co.com.bancolombia.api.dto.EnrolDTO;
import co.com.bancolombia.api.header.ClientHeader;
import co.com.bancolombia.api.mapper.EnrolMapper;
import co.com.bancolombia.commons.enums.Header;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.usecase.client.ClientUseCase;

import static co.com.bancolombia.usecase.commons.BridgeContact.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.BODY_MISSING_ERROR;
import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.HEADERS_MISSING_ERROR;

@Component
@RequiredArgsConstructor
public class ClientHandler {
    private final ClientUseCase clientUseCase;
    private final ValidatorHandler validatorHandler;
    private final EnrolMapper enrolMapper;

    public Mono<ServerResponse> inactivateClient(ServerRequest serverRequest) {
        return ParamsUtil.getClientHeaders(serverRequest)
                .switchIfEmpty(Mono.error(new TechnicalException(HEADERS_MISSING_ERROR)))
                .doOnNext(validatorHandler::validateObjectHeaders)
                .flatMap(ClientHeader::toModel)
                .flatMap(clientUseCase::inactivateClient)
                .flatMap(ResponseUtil::responseOk);
    }

    public Mono<ServerResponse> saveClient(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(EnrolDTO.class)
                .switchIfEmpty(Mono.error(new TechnicalException(BODY_MISSING_ERROR)))
                .map(enrolMapper::toEntity)
                .flatMap(cc -> clientUseCase.saveClient(cc, Boolean.FALSE, getVoucher()))
                .flatMap(ResponseUtil::responseOk);
    }


    public Mono<ServerResponse> updateClient(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(EnrolDTO.class)
                .switchIfEmpty(Mono.error(new TechnicalException(BODY_MISSING_ERROR)))
                .doOnNext(validatorHandler::validateObject)
                .map(enrolMapper::toEntity)
                .flatMap(cli -> clientUseCase.updateClientMono(cli, Boolean.FALSE, getVoucher()))
                .flatMap(ResponseUtil::responseOk);
    }

    public Mono<ServerResponse> deleteClient(ServerRequest serverRequest) {
        return clientUseCase.deleteClient(Long.valueOf(ParamsUtil
                                .getHeader(serverRequest, Header.DOCUMENT_NUMBER_INIT)),
                        Long.valueOf(ParamsUtil.getHeader(serverRequest, Header.DOCUMENT_NUMBER_END)))
                .flatMap(ResponseUtil::responseOk);
    }

}
