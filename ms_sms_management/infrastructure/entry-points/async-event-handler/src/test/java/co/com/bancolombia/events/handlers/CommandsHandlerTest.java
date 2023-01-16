package co.com.bancolombia.events.handlers;

import co.com.bancolombia.usecase.sendalert.SendAlertUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CommandsHandlerTest {
    @InjectMocks
    private CommandsHandler commandsHandler;
    @Mock
    private SendAlertUseCase useCase;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }
/*
    @Test
    void handleSendAlert() {
        when(useCase.sendAlert(any())).thenReturn(Mono.empty());
        StepVerifier.create(commandsHandler.handleSendAlert(new Command<Alert>("alert", "alert",
                Alert.builder().build())))
                .verifyComplete();
    }*/
}
