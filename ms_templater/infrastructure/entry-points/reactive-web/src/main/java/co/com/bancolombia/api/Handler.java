package co.com.bancolombia.api;

import co.com.bancolombia.api.commons.ErrorHandler;
import co.com.bancolombia.api.commons.RequestValidator;
import co.com.bancolombia.api.dto.TemplaterDTO;
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

import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
@RequiredArgsConstructor
public class Handler extends GenericBaseHandler {

    private final CreateTemplateUseCase createTemplateUseCase;
    private final GetTemplateUseCase getTemplateUseCase;
    private final UpdateTemplateUseCase updateTemplateUseCase;
    private final DeleteTemplateUseCase deleteTemplateUseCase;
    private final CreateMessageUseCase createMessageUseCase;
    private final RequestValidator validator;

    protected Mono<ServerResponse> createTemplate(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(TemplaterDTO.class)
                .doOnSuccess(validator::validateBody)
                .flatMap(TemplaterDTO::toModel)
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
        return templaterDTOMono(serverRequest)
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

    protected Mono<Map<String, String>> templaterDTOMono(ServerRequest serverRequest) {
        return Mono.just(setHeaders(serverRequest))
                .switchIfEmpty(Mono.empty());
    }

    protected Mono<ServerResponse> updateTemplate(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(TemplaterDTO.class)
                .doOnSuccess(validator::validateBody)
                .flatMap(TemplaterDTO::toModel)
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

//    protected Mono<ServerResponse> deleteTemplate(ServerRequest serverRequest) {
//        return serverRequest.bodyToMono(DeleteTemplaterDTO.class)
//                .doOnSuccess(validator::validateBody)
//                .flatMap(DeleteTemplaterDTO::toModel)
//                .flatMap(deleteTemplateUseCase::deleteTemplate)
//                .flatMap(responseMap -> ServerResponse.status(HttpStatus.OK)
//                        .contentType(APPLICATION_JSON)
//                        .bodyValue(ResponseDTO.success(Constants.DELETE_MESSAGE, responseMap,
//                                serverRequest)))
//                .onErrorResume(TechnicalException.class, error -> ServerResponse.status(HttpStatus.CONFLICT)
//                        .contentType(APPLICATION_JSON)
//                        .bodyValue(ResponseDTO.failed(ErrorHandler.technical(error), serverRequest)))
//                .onErrorResume(BusinessException.class, error -> ServerResponse.status(HttpStatus.CONFLICT)
//                        .contentType(APPLICATION_JSON)
//                        .bodyValue(ResponseDTO.failed(ErrorHandler.business(error), serverRequest)));
//    }

//    protected Mono<ServerResponse> createMessage(ServerRequest serverRequest) {
//        return serverRequest.bodyToMono(MessageDTO.class)
//                .doOnSuccess(validator::validateBody)
//                .flatMap(MessageDTO::toModel)
//                .map(templateRequest -> templateRequest.headers(setHeaders(serverRequest)))
//                .map(Request::getHeaders)
//                .flatMap(createMessageUseCase::createMessage)
//                .flatMap(templateResponse -> ServerResponse.status(HttpStatus.OK)
//                        .contentType(APPLICATION_JSON)
//                        .bodyValue(ResponseDTO.success(Constants.CREATE_MESSAGE, templateResponse,
//                                serverRequest)))
//                .onErrorResume(TechnicalException.class, error -> ServerResponse.status(HttpStatus.CONFLICT)
//                        .contentType(APPLICATION_JSON)
//                        .bodyValue(ResponseDTO.failed(ErrorHandler.technical(error), serverRequest)))
//                .onErrorResume(BusinessException.class, error -> ServerResponse.status(HttpStatus.CONFLICT)
//                        .contentType(APPLICATION_JSON)
//                        .bodyValue(ResponseDTO.failed(ErrorHandler.business(error), serverRequest)));
//    }
}
