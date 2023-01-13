package co.com.bancolombia.events.handlers;

import co.com.bancolombia.model.message.Alert;
import co.com.bancolombia.usecase.sendalert.SendAlertUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.reactivecommons.api.domain.Command;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CommandsHandlerTest {
    @InjectMocks
    private CommandsHandler commandsHandler;
    @Mock
    private SendAlertUseCase useCase;

    @BeforeEach
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

   /* @Test
    public void handleSendAlert() {
        when(useCase.sendAlert(any())).thenReturn(Mono.empty());
        StepVerifier.create(commandsHandler.handleSendAlert(new Command<Alert>("alert", "alert",
                Alert.builder().build())))
                .verifyComplete();
    }*/
}
