package co.com.bancolombia.usecase.alerttransaction;


import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.alerttransaction.AlertTransaction;
import co.com.bancolombia.model.alerttransaction.gateways.AlertTransactionGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AlertTransactionUseCaseTest {

    @InjectMocks
    private AlertTransactionUseCase useCase;

    @Mock
    private AlertTransactionGateway alertTransactionGateway;

    private final AlertTransaction alertTransaction = new AlertTransaction();

    @BeforeEach
    private void init() {
        alertTransaction.setIdAlert("ALT");
    }

    @Test
    public void findAllAlertTransaction() {
        when(alertTransactionGateway.findAllAlertTransaction(anyString()))
                .thenReturn(Flux.just(alertTransaction));
        StepVerifier
                .create(useCase.findAllAlertTransaction(alertTransaction.getIdAlert()))
                .expectNextCount(1)
                .verifyComplete();
        verify(alertTransactionGateway).findAllAlertTransaction(anyString());
    }

    @Test
    public void saveAlertTransaction() {
        when(alertTransactionGateway.saveAlertTransaction(any()))
                .thenReturn(Mono.just(alertTransaction));
        StepVerifier
                .create(useCase.saveAlertTransaction(alertTransaction))
                .assertNext(response -> response
                        .getIdAlert().equals(alertTransaction.getIdAlert()))
                .verifyComplete();
        verify(alertTransactionGateway).saveAlertTransaction(any());
    }

    @Test
    public void deleteAlertTransaction() {
        when(alertTransactionGateway.deleteAlertTransaction(any()))
                .thenReturn(Mono.just(alertTransaction.getIdAlert()));
        StepVerifier.create(useCase.deleteAlertTransaction(alertTransaction))
                .expectNextCount(1)
                .verifyComplete();
        verify(alertTransactionGateway).deleteAlertTransaction(any());
    }

    @Test
    public void deleteAlertTransactionWithException() {
        when(alertTransactionGateway.deleteAlertTransaction(any()))
                .thenReturn(Mono.empty());
        useCase.deleteAlertTransaction(alertTransaction)
                .as(StepVerifier::create)
                .expectError(BusinessException.class)
                .verify();
    }
}
