package co.com.bancolombia.events.handlers;


import co.com.bancolombia.model.log.Log;
import co.com.bancolombia.usecase.log.LogUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.reactivecommons.api.domain.Command;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import javax.xml.validation.ValidatorHandler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CommandsHandlerTest {
    @InjectMocks
    private CommandsHandler commandsHandler;
    @Mock
    private LogUseCase useCase;
    @Mock
    private ValidatorHandler validatorHandler;


    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void handleSendSaveClient() {
        when(useCase.saveLog(any())).thenReturn(Mono.empty());
        StepVerifier.create(commandsHandler.saveLog(new Command<Log>("alert", "alert",
                        new Log())))
                .verifyComplete();
    }

}
