package co.com.bancolombia.events.handlers;

import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.events.commons.AbstractConverter;
import co.com.bancolombia.model.log.LoggerBuilder;
import co.com.bancolombia.model.message.Alert;
import co.com.bancolombia.model.message.Response;
import co.com.bancolombia.usecase.log.LogUseCase;
import co.com.bancolombia.usecase.sendalert.SendAlertUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.reactivecommons.api.domain.Command;
import org.reactivecommons.async.commons.communications.Message;
import org.reactivecommons.async.impl.config.annotations.EnableCommandListeners;
import reactor.core.publisher.Mono;

import java.util.function.Predicate;

@EnableCommandListeners
public class CommandsHandler extends AbstractConverter {
    private final SendAlertUseCase useCase;
    private final LogUseCase logUseCase;

    private static final Predicate<Alert> isValidCount = alert ->
            (alert.getHeaders().containsKey("x-death")) && ((Object) alert.getHeaders().get("x-death")).toString()
                    .contains("count=" + alert.getHeaders().get("retryNumber"));

    public CommandsHandler(ObjectMapper objectMapper, LoggerBuilder loggerBuilder, SendAlertUseCase useCase,
                           LogUseCase logUseCase) {
        super(objectMapper,loggerBuilder);
        this.useCase = useCase;
        this.logUseCase = logUseCase;
    }

    public Mono<Void> handleSendAlert(Command<Message> command) {
        return converterMessage(command.getData(), Alert.class)
                .flatMap(this::sendAlert);
    }

    public Mono<Void> sendAlert(Alert alert) {
        return Mono.just(alert)
                .flatMap(useCase::sendAlert)
                .onErrorResume(BusinessException.class, e -> handleExceptions(e, alert))
                .onErrorResume(TechnicalException.class, e -> handleExceptions(e, alert))
                .onErrorResume(e ->  !(e instanceof TechnicalException) && !(e instanceof BusinessException),
                        e -> handleExceptions(e, alert))
                .then(Mono.empty());
    }

    private Mono<Response> handleExceptions(BusinessException businessException, Alert pAlert) {
        return Mono.just(pAlert)
                .filter(alert -> isValidCount.test(alert) || !businessException.getBusinessErrorMessage().getRetry())
                .flatMap(alert -> logUseCase.handlerLog(pAlert, "SMS", Response.builder().code(1)
                        .description(businessException.getMessage()).build(), true))
                .switchIfEmpty(Mono.error(businessException));
    }

    private Mono<Response> handleExceptions(TechnicalException technicalException, Alert pAlert) {
        return Mono.just(pAlert)
                .filter(alert -> isValidCount.test(alert) || !technicalException.getException().getRetry())
                .flatMap(alert -> logUseCase.handlerLog(pAlert, "SMS", Response.builder()
                        .code(technicalException.getCode()).description(technicalException.getMessage()).build(), true))
                .switchIfEmpty(Mono.error(technicalException));
    }
    private Mono<Response> handleExceptions(Throwable throwable, Alert pAlert) {
        return Mono.just(pAlert)
                .filter(isValidCount)
                .flatMap(alert -> logUseCase.handlerLog(pAlert, "SMS", Response.builder().code(1)
                        .description(throwable.getMessage()).build(), true))
                .switchIfEmpty(Mono.error(throwable));
    }

}
