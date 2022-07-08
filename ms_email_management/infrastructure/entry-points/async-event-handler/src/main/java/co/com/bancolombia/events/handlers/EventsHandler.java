package co.com.bancolombia.events.handlers;

import co.com.bancolombia.model.message.Alert;
import co.com.bancolombia.usecase.sendalert.SendAlertUseCase;
import lombok.AllArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.reactivecommons.api.domain.DomainEvent;
import org.reactivecommons.async.impl.config.annotations.EnableEventListeners;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@EnableEventListeners
public class EventsHandler {
    private final SendAlertUseCase useCase;
    private final Log LOGGER = LogFactory.getLog(EventsHandler.class);

    public Mono<Void> handleSendAlert(DomainEvent<Alert> event) {
        LOGGER.info("intento de envio ");
        return useCase.sendAlert(event.getData());
    }
}
