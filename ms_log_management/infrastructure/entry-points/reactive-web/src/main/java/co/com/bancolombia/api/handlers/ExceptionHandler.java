package co.com.bancolombia.api.handlers;

import co.com.bancolombia.api.ResponseUtil;
import co.com.bancolombia.logging.technical.LoggerFactory;
import co.com.bancolombia.logging.technical.logger.TechLogger;
import co.com.bancolombia.logging.technical.message.ObjectTechMsg;
import co.com.bancolombia.commons.enums.TechnicalExceptionEnum;
import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.model.error.Error;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Component
@Order(-2)
public class ExceptionHandler extends AbstractErrorWebExceptionHandler {

    private final Environment environment;
    private static final TechLogger LOGGER = LoggerFactory.getLog(ExceptionHandler.class.getName());

    public ExceptionHandler(ErrorAttributes errorAttributes, ResourceProperties resourceProperties,
                            ApplicationContext applicationContext,
                            ServerCodecConfigurer configurer,
                            Environment environment) {
        super(errorAttributes, resourceProperties, applicationContext);
        this.setMessageWriters(configurer.getWriters());
        this.environment = environment;
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        Throwable throwable = getError(request);

        return Mono.just(request)
                .map(this::getError)
                .flatMap(Mono::error)
                .onErrorResume(TechnicalException.class, this::buildErrorResponse)
                .onErrorResume(BusinessException.class, this::buildErrorResponse)
                .onErrorResume(this::buildErrorResponse)
                .cast(Error.class)
                .map(errorResponse -> errorResponse.toBuilder()
                        .domain(request.path())
                        .build())
                .doAfterTerminate(() -> LOGGER.error(buildObjectTechMsg(throwable, request)))
                .flatMap(ResponseUtil::responseFail);
    }

    private Mono<Error> buildErrorResponse(TechnicalException ex) {
        return Mono.just(Error.builder()
                .reason(ex.getMessage())
                .code(ex.getException().getCode())
                .message(ex.getException().getMessage())
                .build()
        );
    }

    private Mono<Error> buildErrorResponse(BusinessException ex) {
        return Mono.just(Error.builder()
                .reason(ex.getMessage())
                .code(ex.getBusinessErrorMessage().getCode())
                .message(ex.getBusinessErrorMessage().getMessage())
                .build()
        );
    }

    private Mono<Error> buildErrorResponse(Throwable throwable) {
        return Mono.just(Error.builder()
                .reason(throwable.getMessage())
                .code(TechnicalExceptionEnum.INTERNAL_SERVER_ERROR.getCode())
                .message(TechnicalExceptionEnum.INTERNAL_SERVER_ERROR.getMessage())
                .build()
        );
    }


    private ObjectTechMsg<Map<String, Object>> buildObjectTechMsg(Throwable throwable, ServerRequest request) {
        return new ObjectTechMsg(null,
                environment.getProperty("spring.application.name"),
                request.path(),
                Arrays.stream(throwable.getStackTrace())
                        .findFirst()
                        .map(StackTraceElement::getClassName)
                        .orElse(""),
                List.of(), buildStackTrace(throwable));
    }

    private Map<String, Object> buildStackTrace(Throwable throwable) {

        return Map.of("stackTrace", Optional.ofNullable(throwable.getStackTrace()).orElse(new StackTraceElement[]{}),
                "message", Optional.ofNullable(throwable.getMessage()).orElse(""),
                "exception", throwable.toString());
    }

}
