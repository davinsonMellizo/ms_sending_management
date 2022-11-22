package co.com.bancolombia.events.handlers;

import co.com.bancolombia.model.message.Alert;
import co.com.bancolombia.usecase.sendalert.SendAlertUseCase;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.reactivecommons.api.domain.Command;
import org.reactivecommons.api.domain.DomainEvent;
import org.reactivecommons.async.impl.config.annotations.EnableEventListeners;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class EventsHandlerTest {

    @InjectMocks
    private EventsHandler eventsHandler;
    @Mock
    private SendAlertUseCase useCase;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void handleSendAlert() {
        when(useCase.sendAlert(any())).thenReturn(Mono.empty());
        StepVerifier.create(eventsHandler.handleSendAlert(new DomainEvent<>("alert", "alert",
                Alert.builder().build())))
                .verifyComplete();
    }
}
