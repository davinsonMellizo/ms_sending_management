package co.com.bancolombia.events.handlers;


import co.com.bancolombia.model.message.Alert;
import co.com.bancolombia.usecase.sendalert.SendAlertUseCase;
import lombok.AllArgsConstructor;
import org.reactivecommons.api.domain.Command;
import org.reactivecommons.async.impl.config.annotations.EnableCommandListeners;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@EnableCommandListeners
public class CommandsHandler {

    private final SendAlertUseCase useCase;
    private final Log LOGGER = LogFactory.getLog(EventsHandler.class);

    public Mono<Void> handleSendAlert(Command<Alert> command) {
        LOGGER.info("intento de envio command");
        return useCase.sendAlert(command.getData());
    }
}
