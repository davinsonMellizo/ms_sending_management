package co.com.bancolombia.api.commons.handlers;

import co.com.bancolombia.api.commons.util.ResponseUtil;
import co.com.bancolombia.commons.enums.TechnicalExceptionEnum;
import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.log.LoggerBuilder;
import co.com.bancolombia.model.error.Error;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;


@Component
@Order(-2)
public class ExceptionHandler extends AbstractErrorWebExceptionHandler {

    private final Environment environment;
    private final LoggerBuilder logger;

    public ExceptionHandler(ErrorAttributes errorAttributes, WebProperties webProperties,
                            ApplicationContext applicationContext,
                            ServerCodecConfigurer configurator,
                            Environment environment, final LoggerBuilder loggerBuilder) {
        super(errorAttributes, webProperties.getResources(), applicationContext);
        this.setMessageWriters(configurator.getWriters());
        this.environment = environment;
        this.logger = loggerBuilder;
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        var throwable = getError(request);

        return Mono.just(request)
                .map(this::getError)
                .flatMap(Mono::error)
                .onErrorResume(TechnicalException.class, this::responseTechnical)
                .onErrorResume(BusinessException.class, this::responseBusiness)
                .onErrorResume(ResponseStatusException.class, this::responseStatusException)
                .onErrorResume(this::responseAnyError)
                .cast(Error.class)
                .map(errorResponse -> errorResponse.toBuilder()
                        .domain(request.path())
                        .build())
                .doAfterTerminate(() -> logger.error(throwable))
                .flatMap(e -> ResponseUtil.buildResponse(HttpStatus
                        .valueOf(Integer.valueOf(e.getHttpStatus())), e));
    }

    private Mono<Error> responseTechnical(TechnicalException ex) {
        return Mono.just(Error.builder()
                .httpStatus(ex.getException().getHttpStatus())
                .reason(ex.getMessage())
                .code(ex.getException().getCode())
                .message(ex.getException().getMessage())
                .build()
        );
    }

    private Mono<Error> responseBusiness(BusinessException ex) {
        return Mono.just(Error.builder()
                .httpStatus(ex.getException().getHttpStatus())
                .reason(ex.getMessage())
                .code(ex.getException().getCode())
                .message(ex.getException().getMessage())
                .build()
        );
    }

    private Mono<Error> responseStatusException(ResponseStatusException ex) {
        return Mono.just(Error.builder()
                .httpStatus(String.valueOf(ex.getStatus().value()))
                .reason(ex.getReason())
                .code(TechnicalExceptionEnum.INTERNAL_SERVER_ERROR.getCode())
                .message(ex.getMessage())
                .build()
        );
    }

    private Mono<Error> responseAnyError(Throwable throwable) {
        return Mono.just(Error.builder()
                .httpStatus(TechnicalExceptionEnum.INTERNAL_SERVER_ERROR.getHttpStatus())
                .reason(throwable.getMessage())
                .code(TechnicalExceptionEnum.INTERNAL_SERVER_ERROR.getCode())
                .message(TechnicalExceptionEnum.INTERNAL_SERVER_ERROR.getMessage())
                .build()
        );
    }
}
