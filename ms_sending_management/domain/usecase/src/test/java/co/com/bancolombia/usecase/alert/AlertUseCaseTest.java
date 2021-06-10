package co.com.bancolombia.usecase.alert;


import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.alert.gateways.AlertGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AlertUseCaseTest {

    @InjectMocks
    private AlertUseCase useCase;

    @Mock
    private AlertGateway alertGateway;

    private final Alert alert = new Alert();

    @BeforeEach
    private void init() {
        alert.setId("ALT");
    }

    @Test
    public void findAllAlertByClient() {
        when(alertGateway.findAlertById(anyString()))
                .thenReturn(Mono.just(alert));
        StepVerifier
                .create(useCase.findAlertByIdRequest("ALT"))
                .expectNextCount(1)
                .verifyComplete();
        verify(alertGateway).findAlertById(anyString());
    }

    @Test
    public void saveAlert() {
        when(alertGateway.saveAlert(any()))
                .thenReturn(Mono.just(alert));
        StepVerifier
                .create(useCase.saveAlertRequest(alert))
                .assertNext(response -> response
                        .getId().equals(alert.getId()))
                .verifyComplete();
        verify(alertGateway).saveAlert(any());
    }

    @Test
    public void updateAlert() {
        when(alertGateway.updateAlert(any(), any()))
                .thenReturn(Mono.just(alert));
        when(alertGateway.findAlertById(anyString()))
                .thenReturn(Mono.just(alert));
        StepVerifier
                .create(useCase.updateAlertRequest(alert))
                .assertNext(response -> response
                        .getActual().getId()
                        .equals(alert.getId()))
                .verifyComplete();
        verify(alertGateway).updateAlert(any(), any());
        verify(alertGateway).findAlertById(any());
    }

    @Test
    public void deleteAlert() {
        when(alertGateway.findAlertById(anyString()))
                .thenReturn(Mono.just(alert));
        when(alertGateway.deleteAlert(any()))
                .thenReturn(Mono.just("ALT"));
        StepVerifier.create(useCase.deleteAlertRequest(alert.getId()))
                .expectNextCount(1)
                .verifyComplete();
        verify(alertGateway).deleteAlert(any());
    }

    @Test
    public void updateAlertWithException() {
        when(alertGateway.findAlertById(anyString()))
                .thenReturn(Mono.empty());
        useCase.updateAlertRequest(alert)
                .as(StepVerifier::create)
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    public void deleteAlertWithException() {
        when(alertGateway.findAlertById(anyString()))
                .thenReturn(Mono.empty());
        useCase.deleteAlertRequest("ALT")
                .as(StepVerifier::create)
                .expectError(BusinessException.class)
                .verify();
    }
}
