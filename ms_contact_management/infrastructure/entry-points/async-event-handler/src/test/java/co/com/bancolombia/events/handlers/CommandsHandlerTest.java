package co.com.bancolombia.events.handlers;

import co.com.bancolombia.api.commons.handlers.ValidatorHandler;
import co.com.bancolombia.api.mapper.EnrolMapper;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.usecase.client.ClientUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.reactivecommons.api.domain.Command;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.BODY_MISSING_ERROR;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class CommandsHandlerTest {
    @InjectMocks
    private CommandsHandler commandsHandler;
    @Mock
    private ClientUseCase clientUseCase;
    @Mock
    private ValidatorHandler validatorHandler;
    @Spy
    private EnrolMapper mapper = Mappers.getMapper(EnrolMapper.class);


    @BeforeEach
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void handleSendSaveClient() {
        when(clientUseCase.saveClient(any(), anyBoolean(), anyString())).thenReturn(Mono.empty());
        StepVerifier.create(commandsHandler.saveClient(new Command<>("alert", "alert",
                "{}")))
                .verifyComplete();
    }
    @Test
    void handleSendUpdateClient() {
        when(clientUseCase.updateClientMono(any(), anyBoolean(), anyString())).thenReturn(Mono.empty());
        StepVerifier.create(commandsHandler.updateClient(new Command<>("alert", "alert",
                "{}")))
                .verifyComplete();
    }
    @Test
    void handleSendUpdateClientError() {
        when(clientUseCase.updateClientMono(any(), anyBoolean(), anyString()))
                .thenReturn(Mono.error(new TechnicalException(BODY_MISSING_ERROR)));
        StepVerifier.create(commandsHandler.updateClient(new Command<>("alert", "alert",
                "{}")))
                .verifyComplete();
    }

}
