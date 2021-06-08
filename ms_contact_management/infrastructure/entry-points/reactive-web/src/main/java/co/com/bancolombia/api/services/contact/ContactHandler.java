package co.com.bancolombia.api.services.contact;

import co.com.bancolombia.api.commons.handlers.ValidatorHandler;
import co.com.bancolombia.api.commons.util.ParamsUtil;
import co.com.bancolombia.api.commons.util.ResponseUtil;
import co.com.bancolombia.api.dto.Contact;
import co.com.bancolombia.api.header.ClientHeader;
import co.com.bancolombia.api.header.ContactHeader;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.usecase.contact.ContactUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.BODY_MISSING_ERROR;
import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.HEADERS_MISSING_ERROR;

@Component
@RequiredArgsConstructor
public class ContactHandler {
    private final ContactUseCase contactUseCase;
    private final ValidatorHandler validatorHandler;

    public Mono<ServerResponse> findContacts(ServerRequest serverRequest) {
        return ParamsUtil.getClientHeaders(serverRequest)
                .switchIfEmpty(Mono.error(new TechnicalException(HEADERS_MISSING_ERROR)))
                .doOnNext(validatorHandler::validateObjectHeaders)
                .flatMap(ClientHeader::toModel)
                .flatMap(contactUseCase::findContactsByClient)
                .flatMap(ResponseUtil::responseOk);
    }

    public Mono<ServerResponse> saveContact(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(Contact.class)
                .switchIfEmpty(Mono.error(new TechnicalException(BODY_MISSING_ERROR)))
                .doOnNext(validatorHandler::validateObject)
                .flatMap(Contact::toModel)
                .flatMap(contactUseCase::saveContact)
                .flatMap(ResponseUtil::responseOk);
    }

    public Mono<ServerResponse> updateContact(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(Contact.class)
                .switchIfEmpty(Mono.error(new TechnicalException(BODY_MISSING_ERROR)))
                .doOnNext(validatorHandler::validateObject)
                .flatMap(Contact::toModel)
                .flatMap(contactUseCase::updateContact)
                .flatMap(ResponseUtil::responseOk);
    }

    public Mono<ServerResponse> deleteContact(ServerRequest serverRequest) {
        return ParamsUtil.getContactHeaders(serverRequest)
                .switchIfEmpty(Mono.error(new TechnicalException(HEADERS_MISSING_ERROR)))
                .doOnNext(validatorHandler::validateObjectHeaders)
                .flatMap(ContactHeader::toModel)
                .flatMap(contactUseCase::deleteContact)
                .flatMap(ResponseUtil::responseOk);
    }

}
