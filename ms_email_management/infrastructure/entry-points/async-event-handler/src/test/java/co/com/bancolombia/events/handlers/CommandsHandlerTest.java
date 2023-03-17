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
    public void init(){
        MockitoAnnotations.initMocks(this);
    }


}
