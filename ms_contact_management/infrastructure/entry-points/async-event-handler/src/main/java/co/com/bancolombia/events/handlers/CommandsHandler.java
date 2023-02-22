package co.com.bancolombia.events.handlers;

import co.com.bancolombia.api.commons.handlers.ValidatorHandler;
import co.com.bancolombia.api.dto.EnrolDTO;
import co.com.bancolombia.api.mapper.EnrolMapper;
import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.log.LoggerBuilder;
import co.com.bancolombia.usecase.client.ClientUseCase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.reactivecommons.api.domain.Command;
import org.reactivecommons.async.impl.config.annotations.EnableCommandListeners;
import reactor.core.publisher.Mono;
import reactor.function.Function4;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.BODY_MISSING_ERROR;
import static co.com.bancolombia.usecase.commons.BridgeContact.getVoucher;


@AllArgsConstructor
@EnableCommandListeners
public class CommandsHandler {

    private final LoggerBuilder logger;
    private final ClientUseCase useCase;
    private final EnrolMapper enrolIseriesMapper;
    private final ValidatorHandler validatorBodyHandler;

    public Mono<Void> saveClient(Command<String> command){
        return handleSendAlert(command.getData(), useCase::saveClient);
    }
    public Mono<Void> updateClient(Command<String> command){
        return handleSendAlert(command.getData(), useCase::updateClient);
    }

    private  <M, R> Mono<Void> handleSendAlert(String data, Function4<M,Boolean,String, Boolean, Mono<R>> use) {
        var mapper = new ObjectMapper();
        try {
            Object mstCode = mapper.readValue(data, EnrolDTO.class);
            return Mono.just((EnrolDTO)mstCode)
                    .switchIfEmpty(Mono.error(new TechnicalException(BODY_MISSING_ERROR)))
                    .doOnNext(validatorBodyHandler::validateObject)
                    .map(enrolIseriesMapper::toEntity)
                    .map(m  -> (M)m)
                    .flatMap(m ->  use.apply(m, Boolean.TRUE, getVoucher(), Boolean.FALSE))
                    .onErrorResume(BusinessException.class, e -> Mono.empty())
                    .onErrorResume(TechnicalException.class, this::onErrorByData)
                    .then();
        } catch (JsonProcessingException e) {
            logger.error(e);
        }
        return Mono.error(new Throwable());
    }

    private <M> Mono<M> onErrorByData(TechnicalException exception){
        return Mono.just(exception)
                .filter(e -> !e.getException().equals(BODY_MISSING_ERROR))
                .flatMap(Mono::error);
    }

}
