package co.com.bancolombia.events.handlers;

import co.com.bancolombia.api.commons.handlers.ValidatorHandler;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.usecase.alert.AlertUseCase;
import co.com.bancolombia.usecase.alerttransaction.AlertTransactionUseCase;
import co.com.bancolombia.usecase.consumer.ConsumerUseCase;
import co.com.bancolombia.usecase.provider.ProviderUseCase;
import co.com.bancolombia.usecase.remitter.RemitterUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.reactivecommons.api.domain.Command;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.BODY_MISSING_ERROR;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CommandsHandlerTest {
    @InjectMocks
    private CommandsHandler commandsHandler;
    @Mock
    private ProviderUseCase useCaseProvider;
    @Mock
    private RemitterUseCase useCaseRemitter;
    @Mock
    private AlertTransactionUseCase useCaseAlertTrx;
    @Mock
    private AlertUseCase useCaseAlert;
    @Mock
    private ConsumerUseCase useCaseConsumer;
    @Mock
    private ValidatorHandler validatorHandler;


    @BeforeEach
    public void init(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handleSendSaveProvider() {
        when(useCaseProvider.saveProvider(any())).thenReturn(Mono.empty());
        StepVerifier.create(commandsHandler.saveProvider(new Command<>("alert", "alert",
                "{}")))
                .verifyComplete();
    }
    @Test
    void handleSendUpdateProvider() {
        when(useCaseProvider.updateProvider(any())).thenReturn(Mono.empty());
        StepVerifier.create(commandsHandler.updateProvider(new Command<>("alert", "alert",
                "{}")))
                .verifyComplete();
    }
    @Test
    void handleSendSaveRemitter() {
        when(useCaseRemitter.saveRemitter(any())).thenReturn(Mono.empty());
        StepVerifier.create(commandsHandler.saveRemitter(new Command<>("alert", "alert",
                "{}")))
                .verifyComplete();
    }
    @Test
    void handleSendUpdateRemitter() {
        when(useCaseRemitter.updateRemitter(any())).thenReturn(Mono.empty());
        StepVerifier.create(commandsHandler.updateRemitter(new Command<>("alert", "alert",
                "{}")))
                .verifyComplete();
    }
    @Test
    void handleSendSaveAlert() {
        when(useCaseAlert.saveAlertRequest(any())).thenReturn(Mono.empty());
        StepVerifier.create(commandsHandler.saveAlert(new Command<>("alert", "alert",
                "{}")))
                .verifyComplete();
    }
    @Test
    void handleSendUpdateAlert() {
        when(useCaseAlert.updateAlertRequest(any())).thenReturn(Mono.empty());
        StepVerifier.create(commandsHandler.updateAlert(new Command<>("alert", "alert",
                "{}")))
                .verifyComplete();
    }
    @Test
    void handleSendSaveConsumer() {
        when(useCaseConsumer.saveConsumer(any())).thenReturn(Mono.empty());
        StepVerifier.create(commandsHandler.saveConsumer(new Command<>("alert", "alert",
                "{}")))
                .verifyComplete();
    }
    @Test
    void handleSendUpdateConsumer() {
        when(useCaseConsumer.updateConsumer(any())).thenReturn(Mono.empty());
        StepVerifier.create(commandsHandler.updateConsumer(new Command<>("alert", "alert",
                "{}")))
                .verifyComplete();
    }
    @Test
    void handleSendSaveAlertTrx() {
        when(useCaseAlertTrx.saveAlertTransaction(any())).thenReturn(Mono.empty());
        StepVerifier.create(commandsHandler.saveAlertTrx(new Command<>("alert", "alert",
                "{}")))
                .verifyComplete();
    }
    @Test
    void handleSendDeleteAlertTrx() {
        when(useCaseAlertTrx.deleteAlertTransaction(any()))
                .thenReturn(Mono.error(new TechnicalException(BODY_MISSING_ERROR)));
        StepVerifier.create(commandsHandler.deleteAlertTrx(new Command<>("alert", "alert",
                "{}")))
                .verifyComplete();
    }

}
