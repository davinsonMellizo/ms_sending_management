package co.com.bancolombia.events.handlers;

import co.com.bancolombia.config.model.message.Message;
import co.com.bancolombia.usecase.sendalert.ManagementAlertUseCase;
import lombok.AllArgsConstructor;
import org.reactivecommons.api.domain.Command;
import org.reactivecommons.async.impl.config.annotations.EnableCommandListeners;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@EnableCommandListeners
public class CommandsHandler {
    private final ManagementAlertUseCase useCase;

    public Mono<Void> handleSendAlert(Command<Message> command) {
        return useCase.alertSendingManager(command.getData());
    }
}
