package co.com.bancolombia.api.services.client;

import co.com.bancolombia.api.commons.handlers.ValidatorHandler;
import co.com.bancolombia.api.commons.parameters.Parameters;
import co.com.bancolombia.api.commons.util.ParamsUtil;
import co.com.bancolombia.api.commons.util.ResponseUtil;
import co.com.bancolombia.api.dto.EnrolDTO;
import co.com.bancolombia.api.dto.IdentificationDTO;
import co.com.bancolombia.api.mapper.EnrolMapper;
import co.com.bancolombia.commons.enums.Header;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.usecase.client.ClientUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.BODY_MISSING_ERROR;
import static co.com.bancolombia.usecase.commons.BridgeContact.getVoucher;

@Component
@RequiredArgsConstructor
public class ClientHandler {
    private final ClientUseCase clientUseCase;
    private final ValidatorHandler validatorHandler;
    private final EnrolMapper enrolMapper;
    private final Parameters parameters;

    public Mono<ServerResponse> inactivateClient(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(IdentificationDTO.class)
                .switchIfEmpty(Mono.error(new TechnicalException(BODY_MISSING_ERROR)))
                .doOnNext(validatorHandler::validateObjectHeaders)
                .flatMap(IdentificationDTO::toClient)
                .map(client -> client.toBuilder().enrollmentOrigin(ParamsUtil.getConsumerCode(serverRequest)).build())
                .flatMap(clientUseCase::inactivateClient)
                .flatMap(ResponseUtil::responseOk);
    }

    public Mono<ServerResponse> saveClient(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(EnrolDTO.class)
                .switchIfEmpty(Mono.error(new TechnicalException(BODY_MISSING_ERROR)))
                .doOnNext(validatorHandler::validateObject)
                .map(enrolMapper::toEntity)
                .flatMap(enrol -> clientUseCase.saveClient(enrol, Boolean.FALSE, getVoucher(),
                        parameters.getSynchronizeIsiries()))
                .flatMap(ResponseUtil::responseOk);
    }

    public Mono<ServerResponse> updateClient(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(EnrolDTO.class)
                .switchIfEmpty(Mono.error(new TechnicalException(BODY_MISSING_ERROR)))
                .doOnNext(validatorHandler::validateObject)
                .map(enrolMapper::toEntity)
                .flatMap(enrol -> clientUseCase.updateClient(enrol, Boolean.FALSE, getVoucher(),
                        parameters.getSynchronizeIsiries()))
                .flatMap(ResponseUtil::responseOk);
    }

    public Mono<ServerResponse> deleteClient(ServerRequest serverRequest) {
        return clientUseCase.deleteClient(Long.valueOf(ParamsUtil
                                .getHeader(serverRequest, Header.DOCUMENT_NUMBER_INIT)),
                        Long.valueOf(ParamsUtil.getHeader(serverRequest, Header.DOCUMENT_NUMBER_END)))
                .flatMap(ResponseUtil::responseOk);
    }

}
