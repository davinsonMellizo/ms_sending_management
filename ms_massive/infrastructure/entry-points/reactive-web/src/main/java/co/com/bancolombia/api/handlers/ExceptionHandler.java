package co.com.bancolombia.api.handlers;

import co.com.bancolombia.api.util.ResponseUtil;
import co.com.bancolombia.commons.exception.BusinessException;
import co.com.bancolombia.commons.exception.TechnicalException;
import co.com.bancolombia.model.error.Error;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.INTERNAL_SERVER_ERROR;


@Component
@Order(-2)
public class ExceptionHandler extends AbstractErrorWebExceptionHandler {

    private final Environment environment;

    public ExceptionHandler(ErrorAttributes errorAttributes, WebProperties webProperties,
                            ApplicationContext applicationContext,
                            ServerCodecConfigurer configurator,
                            Environment environment) {
        super(errorAttributes, webProperties.getResources(), applicationContext);
        this.setMessageWriters(configurator.getWriters());
        this.environment = environment;
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
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
                .code(ex.getException().getCode())
                .message(ex.getException().getMessage())
                .build()
        );
    }

    private Mono<Error> buildErrorResponse(Throwable throwable) {
        return Mono.just(Error.builder()
                .reason(throwable.getMessage())
                .code(INTERNAL_SERVER_ERROR.getCode())
                .message(INTERNAL_SERVER_ERROR.getMessage())
                .build()
        );
    }
}
