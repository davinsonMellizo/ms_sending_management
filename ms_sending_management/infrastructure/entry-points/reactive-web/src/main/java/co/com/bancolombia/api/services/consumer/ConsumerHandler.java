package co.com.bancolombia.api.services.consumer;

import co.com.bancolombia.api.commons.handlers.ValidatorHandler;
import co.com.bancolombia.api.commons.util.ParamsUtil;
import co.com.bancolombia.api.commons.util.ResponseUtil;
import co.com.bancolombia.api.dto.ConsumerDTO;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.usecase.consumer.ConsumerUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.BODY_MISSING_ERROR;

@Component
@RequiredArgsConstructor
public class ConsumerHandler {

    private final ConsumerUseCase useCase;
    private final ValidatorHandler validatorHandler;

    public Mono<ServerResponse> findConsumer(ServerRequest serverRequest) {
        return ParamsUtil.getId(serverRequest)
                .doOnNext(System.out::println)
                .flatMap(useCase::findConsumerById)
                .flatMap(ResponseUtil::responseOk);
    }

    public Mono<ServerResponse> findAllConsumer(ServerRequest serverRequest) {
        return useCase.findAllConsumer()
                .flatMap(ResponseUtil::responseOk);
    }

    public Mono<ServerResponse> saveConsumer(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(ConsumerDTO.class)
                .switchIfEmpty(Mono.error(new TechnicalException(BODY_MISSING_ERROR)))
                .doOnNext(validatorHandler::validateObject)
                .flatMap(ConsumerDTO::toModel)
                .flatMap(useCase::saveConsumer)
                .flatMap(ResponseUtil::responseOk);
    }

    public Mono<ServerResponse> updateConsumer(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(ConsumerDTO.class)
                .switchIfEmpty(Mono.error(new TechnicalException(BODY_MISSING_ERROR)))
                .doOnNext(validatorHandler::validateObject)
                .flatMap(ConsumerDTO::toModel)
                .flatMap(useCase::updateConsumer)
                .flatMap(ResponseUtil::responseOk);
    }

    public Mono<ServerResponse> deleteConsumer(ServerRequest serverRequest) {
        return ParamsUtil.getId(serverRequest)
                .flatMap(useCase::deleteConsumerById)
                .flatMap(ResponseUtil::responseOk);
    }
}
