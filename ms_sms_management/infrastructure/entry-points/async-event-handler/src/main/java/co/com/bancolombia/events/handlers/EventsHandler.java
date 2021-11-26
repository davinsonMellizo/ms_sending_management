package co.com.bancolombia.events.handlers;

import co.com.bancolombia.model.message.Alert;
import co.com.bancolombia.usecase.sendalert.SendAlertUseCase;
import lombok.AllArgsConstructor;
import org.reactivecommons.api.domain.DomainEvent;
import org.reactivecommons.async.impl.config.annotations.EnableEventListeners;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@EnableEventListeners
public class EventsHandler {
    private final SendAlertUseCase useCase;

    public Mono<Void> handleSendAlert(DomainEvent<Alert> event) {
        return useCase.sendAlertToProviders(event.getData());
    }
}
