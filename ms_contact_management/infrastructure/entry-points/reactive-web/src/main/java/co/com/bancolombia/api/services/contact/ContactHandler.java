package co.com.bancolombia.api.services.contact;

import co.com.bancolombia.api.commons.handlers.ValidatorHandler;
import co.com.bancolombia.api.commons.util.ParamsUtil;
import co.com.bancolombia.api.commons.util.ResponseUtil;
import co.com.bancolombia.api.dto.ResponseContactsDTO;
import co.com.bancolombia.api.header.ClientHeader;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.usecase.contact.ContactUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.HEADERS_MISSING_ERROR;

@Component
@RequiredArgsConstructor
public class ContactHandler {
    private final ContactUseCase contactUseCase;
    private final ValidatorHandler validatorHandler;

    public Mono<ServerResponse> findClient(ServerRequest serverRequest) {
        return ParamsUtil.getClientHeaders(serverRequest)
                .switchIfEmpty(Mono.error(new TechnicalException(HEADERS_MISSING_ERROR)))
                .doOnNext(validatorHandler::validateObjectHeaders)
                .flatMap(ClientHeader::toModel)
                .flatMap(client ->  contactUseCase.findContactsByClient(client,
                        ParamsUtil.getConsumer(serverRequest)))
                .map(ResponseContactsDTO::new)
                .flatMap(ResponseUtil::responseOk);
    }
}
