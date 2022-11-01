package co.com.bancolombia.events.handlers;

import co.com.bancolombia.api.commons.handlers.ValidatorHandler;
import co.com.bancolombia.api.dto.AlertDTO;
import co.com.bancolombia.api.dto.ProviderDTO;
import co.com.bancolombia.api.dto.RemitterDTO;
import co.com.bancolombia.api.dto.DTO;
import co.com.bancolombia.api.dto.AlertTransactionDTO;
import co.com.bancolombia.api.dto.ConsumerDTO;
import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.model.log.LoggerBuilder;
import co.com.bancolombia.usecase.alert.AlertUseCase;
import co.com.bancolombia.usecase.alerttransaction.AlertTransactionUseCase;
import co.com.bancolombia.usecase.consumer.ConsumerUseCase;
import co.com.bancolombia.usecase.provider.ProviderUseCase;
import co.com.bancolombia.usecase.remitter.RemitterUseCase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.reactivecommons.api.domain.Command;
import org.reactivecommons.async.impl.config.annotations.EnableCommandListeners;
import reactor.core.publisher.Mono;

import java.util.function.Function;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.BODY_MISSING_ERROR;

@AllArgsConstructor
@EnableCommandListeners
public class CommandsHandler {
    private final ProviderUseCase useCaseProvider;
    private final RemitterUseCase useCaseRemitter;
    private final AlertTransactionUseCase useCaseAlertTrx;
    private final AlertUseCase useCaseAlert;
    private final ConsumerUseCase useCaseConsumer;
    private final ValidatorHandler validatorHandler;
    private final LoggerBuilder logger;

    public Mono<Void> saveProvider(Command<String> command) {
        return handleSendAlert(command.getData(), useCaseProvider::saveProvider, ProviderDTO.class);
    }

    public Mono<Void> updateProvider(Command<String> command) {
        return handleSendAlert(command.getData(), useCaseProvider::updateProvider, ProviderDTO.class);
    }

    public Mono<Void> saveRemitter(Command<String> command) {
        return handleSendAlert(command.getData(), useCaseRemitter::saveRemitter, RemitterDTO.class);
    }

    public Mono<Void> updateRemitter(Command<String> command) {
        return handleSendAlert(command.getData(), useCaseRemitter::updateRemitter, RemitterDTO.class);
    }

    public Mono<Void> saveAlert(Command<String> command) {
        return handleSendAlert(command.getData(), useCaseAlert::saveAlertRequest, AlertDTO.class);
    }

    public Mono<Void> updateAlert(Command<String> command) {
        return handleSendAlert(command.getData(), useCaseAlert::updateAlertRequest, AlertDTO.class);
    }

    public Mono<Void> saveAlertTrx(Command<String> command) {
        return handleSendAlert(command.getData(), useCaseAlertTrx::saveAlertTransaction, AlertTransactionDTO.class);
    }

    public Mono<Void> deleteAlertTrx(Command<String> command) {
        return handleSendAlert(command.getData(), useCaseAlertTrx::deleteAlertTransaction, AlertTransactionDTO.class);
    }

    public Mono<Void> saveConsumer(Command<String> command) {
        return handleSendAlert(command.getData(), useCaseConsumer::saveConsumer, ConsumerDTO.class);
    }

    public Mono<Void> updateConsumer(Command<String> command) {
        return handleSendAlert(command.getData(), useCaseConsumer::updateConsumer, ConsumerDTO.class);
    }

    private <T extends DTO<M>, M, R> Mono<Void> handleSendAlert(String data, Function<M, Mono<R>> use, Class<?> clazz) {
        var mapper = new ObjectMapper();
        try {
            Object mstCode = mapper.readValue(data, clazz);
            return Mono.just((T) mstCode)
                    .switchIfEmpty(Mono.error(new TechnicalException(BODY_MISSING_ERROR)))
                    .doOnNext(validatorHandler::validateObject)
                    .flatMap(DTO::toModel)
                    .flatMap(use)
                    .onErrorResume(BusinessException.class, e -> Mono.empty())
                    .onErrorResume(TechnicalException.class, this::onErrorByData)
                    .then();
        } catch (JsonProcessingException e) {
            logger.error(e);
        }
        return Mono.error(new Throwable());
    }

    private <M> Mono<M> onErrorByData(TechnicalException exception) {
        return Mono.just(exception)
                .filter(e -> !e.getException().equals(BODY_MISSING_ERROR))
                .flatMap(Mono::error);
    }
}
