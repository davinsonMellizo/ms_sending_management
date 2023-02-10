package co.com.bancolombia.events.handlers;

import co.com.bancolombia.events.commons.Converter;
import co.com.bancolombia.model.message.Alert;
import co.com.bancolombia.usecase.sendalert.SendAlertUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.java.Log;
import org.reactivecommons.api.domain.Command;
import org.reactivecommons.async.impl.communications.Message;
import org.reactivecommons.async.impl.config.annotations.EnableCommandListeners;
import reactor.core.publisher.Mono;
import java.util.logging.Level;


@Log
@EnableCommandListeners
public class CommandsHandler extends Converter {
    private final SendAlertUseCase useCase;

    public CommandsHandler(ObjectMapper objectMapper, SendAlertUseCase useCase) {
        super(objectMapper);
        this.useCase = useCase;
    }

    public Mono<Void> handleSendAlert(Command<Message> command) {
        return converterMessage(command.getData(), Alert.class)
                .flatMap(useCase::sendAlert);
    }
}
