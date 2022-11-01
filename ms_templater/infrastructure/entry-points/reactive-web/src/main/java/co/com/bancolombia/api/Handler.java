package co.com.bancolombia.api;

import co.com.bancolombia.api.commons.ErrorHandler;
import co.com.bancolombia.api.commons.RequestValidator;
import co.com.bancolombia.api.dto.DeleteTemplateDTO;
import co.com.bancolombia.api.dto.GetTemplateDTO;
import co.com.bancolombia.api.dto.MessageDTO;
import co.com.bancolombia.api.dto.TemplateDTO;
import co.com.bancolombia.api.utils.Params;
import co.com.bancolombia.api.utils.ResponseDTO;
import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.usecase.createmessage.CreateMessageUseCase;
import co.com.bancolombia.usecase.createtemplate.CreateTemplateUseCase;
import co.com.bancolombia.usecase.deletetemplate.DeleteTemplateUseCase;
import co.com.bancolombia.usecase.gettemplate.GetTemplateUseCase;
import co.com.bancolombia.usecase.updatetemplate.UpdateTemplateUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
@RequiredArgsConstructor
public class Handler {

    private final CreateTemplateUseCase createTemplateUseCase;
    private final GetTemplateUseCase getTemplateUseCase;
    private final UpdateTemplateUseCase updateTemplateUseCase;
    private final DeleteTemplateUseCase deleteTemplateUseCase;
    private final CreateMessageUseCase createMessageUseCase;
    private final RequestValidator validator;

    protected Mono<ServerResponse> createTemplate(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(TemplateDTO.class)
                .doOnSuccess(validator::validateBody)
                .flatMap(TemplateDTO::toModel)
                .flatMap(createTemplateUseCase::createTemplate)
                .flatMap(template -> ServerResponse.status(HttpStatus.OK)
                        .contentType(APPLICATION_JSON)
                        .bodyValue(ResponseDTO.success(template, serverRequest)))
                .onErrorResume(TechnicalException.class, error -> ServerResponse.status(HttpStatus.CONFLICT)
                        .contentType(APPLICATION_JSON)
                        .bodyValue(ResponseDTO.failed(ErrorHandler.technical(error), serverRequest)))
                .onErrorResume(BusinessException.class, error -> ServerResponse.status(HttpStatus.CONFLICT)
                        .contentType(APPLICATION_JSON)
                        .bodyValue(ResponseDTO.failed(ErrorHandler.business(error), serverRequest)));
    }

    protected Mono<ServerResponse> getTemplate(ServerRequest serverRequest) {
        return Params.getTemplateParams(serverRequest)
                .doOnNext(validator::validateBody)
                .flatMap(GetTemplateDTO::toModel)
                .flatMap(getTemplateUseCase::getTemplate)
                .flatMap(template -> ServerResponse.status(HttpStatus.OK)
                        .contentType(APPLICATION_JSON)
                        .bodyValue(ResponseDTO.success(template, serverRequest)))
                .onErrorResume(TechnicalException.class, error -> ServerResponse.status(HttpStatus.CONFLICT)
                        .contentType(APPLICATION_JSON)
                        .bodyValue(ResponseDTO.failed(ErrorHandler.technical(error), serverRequest)))
                .onErrorResume(BusinessException.class, error -> ServerResponse.status(HttpStatus.CONFLICT)
                        .contentType(APPLICATION_JSON)
                        .bodyValue(ResponseDTO.failed(ErrorHandler.business(error), serverRequest)));
    }

    protected Mono<ServerResponse> updateTemplate(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(TemplateDTO.class)
                .doOnSuccess(validator::validateBody)
                .flatMap(TemplateDTO::toModel)
                .flatMap(updateTemplateUseCase::updateTemplate)
                .flatMap(response -> ServerResponse.status(HttpStatus.OK)
                        .contentType(APPLICATION_JSON)
                        .bodyValue(ResponseDTO.success(response, serverRequest)))
                .onErrorResume(TechnicalException.class, error -> ServerResponse.status(HttpStatus.CONFLICT)
                        .contentType(APPLICATION_JSON)
                        .bodyValue(ResponseDTO.failed(ErrorHandler.technical(error), serverRequest)))
                .onErrorResume(BusinessException.class, error -> ServerResponse.status(HttpStatus.CONFLICT)
                        .contentType(APPLICATION_JSON)
                        .bodyValue(ResponseDTO.failed(ErrorHandler.business(error), serverRequest)));
    }

    protected Mono<ServerResponse> deleteTemplate(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(DeleteTemplateDTO.class)
                .doOnSuccess(validator::validateBody)
                .flatMap(DeleteTemplateDTO::toModel)
                .flatMap(deleteTemplateUseCase::deleteTemplate)
                .flatMap(response -> ServerResponse.status(HttpStatus.OK)
                        .contentType(APPLICATION_JSON)
                        .bodyValue(ResponseDTO.success(response, serverRequest)))
                .onErrorResume(TechnicalException.class, error -> ServerResponse.status(HttpStatus.CONFLICT)
                        .contentType(APPLICATION_JSON)
                        .bodyValue(ResponseDTO.failed(ErrorHandler.technical(error), serverRequest)))
                .onErrorResume(BusinessException.class, error -> ServerResponse.status(HttpStatus.CONFLICT)
                        .contentType(APPLICATION_JSON)
                        .bodyValue(ResponseDTO.failed(ErrorHandler.business(error), serverRequest)));
    }

    protected Mono<ServerResponse> createMessage(ServerRequest serverRequest) {
        return Params.getMessageParams(serverRequest)
                .doOnNext(validator::validateBody)
                .flatMap(MessageDTO::toModel)
                .flatMap(createMessageUseCase::createMessage)
                .flatMap(message -> ServerResponse.status(HttpStatus.OK)
                        .contentType(APPLICATION_JSON)
                        .bodyValue(ResponseDTO.success(message, serverRequest)))
                .onErrorResume(TechnicalException.class, error -> ServerResponse.status(HttpStatus.CONFLICT)
                        .contentType(APPLICATION_JSON)
                        .bodyValue(ResponseDTO.failed(ErrorHandler.technical(error), serverRequest)))
                .onErrorResume(BusinessException.class, error -> ServerResponse.status(HttpStatus.CONFLICT)
                        .contentType(APPLICATION_JSON)
                        .bodyValue(ResponseDTO.failed(ErrorHandler.business(error), serverRequest)));
    }
}
