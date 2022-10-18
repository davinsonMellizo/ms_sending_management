package co.com.bancolombia.api.services.schedule;

import co.com.bancolombia.api.commons.handlers.ValidatorHandler;
import co.com.bancolombia.api.commons.util.ParamsUtil;
import co.com.bancolombia.api.dto.ResponseDTO;
import co.com.bancolombia.api.dto.ScheduleDTO;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.usecase.schedule.ScheduleUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.BODY_MISSING_ERROR;

@Component
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class ScheduleHandler {
    private final ScheduleUseCase useCase;
    private final ValidatorHandler validatorHandler;

    public Mono<ServerResponse> saveSchedule(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(ScheduleDTO.class)
                .switchIfEmpty(Mono.error(new TechnicalException(BODY_MISSING_ERROR)))
                .doOnNext(validatorHandler::validateObject)
                .flatMap(ScheduleDTO::toModel)
                .flatMap(useCase::saveSchedule)
                .map(schedule -> ResponseDTO.success(schedule, serverRequest))
                .flatMap(ResponseDTO::responseOk);
    }

    public Mono<ServerResponse> findScheduleById(ServerRequest serverRequest) {
        return ParamsUtil.getId(serverRequest)
                .flatMap(id -> useCase.findScheduleById(Long.valueOf(id)))
                .map(schedule -> ResponseDTO.success(schedule, serverRequest))
                .flatMap(ResponseDTO::responseOk);
    }

    public Mono<ServerResponse> updateSchedule(ServerRequest serverRequest) {
        return Mono.zip(ParamsUtil.getId(serverRequest), serverRequest.bodyToMono(ScheduleDTO.class))
                .flatMap(t -> Mono.just(t.getT2())
                        .switchIfEmpty(Mono.error(new TechnicalException(BODY_MISSING_ERROR)))
                        .doOnNext(validatorHandler::validateObject)
                        .flatMap(ScheduleDTO::toModel)
                        .flatMap(schedule -> useCase.updateSchedule(schedule, Long.valueOf(t.getT1())))
                        .map(schedule -> ResponseDTO.success(schedule, serverRequest))
                        .flatMap(ResponseDTO::responseOk));
    }
}
