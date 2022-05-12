package co.com.bancolombia.usecase.alert;


import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.alert.gateways.AlertGateway;
import co.com.bancolombia.model.response.StatusResponse;
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
    void findAlertById() {
        when(alertGateway.findAlertById(anyString()))
                .thenReturn(Mono.just(alert));
        StepVerifier
                .create(useCase.findAlertByIdRequest(alert.getId()))
                .expectNextCount(1)
                .verifyComplete();
        verify(alertGateway).findAlertById(anyString());
    }

    @Test
    void saveAlert() {
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
    void updateAlert() {
        when(alertGateway.updateAlert(any()))
                .thenReturn(Mono.just(StatusResponse.<Alert>builder().actual(alert).before(alert).build()));
        StepVerifier
                .create(useCase.updateAlertRequest(alert))
                .assertNext(response -> response
                        .getActual().getId()
                        .equals(alert.getId()))
                .verifyComplete();
        verify(alertGateway).updateAlert(any());
    }

    @Test
    void deleteAlert() {
        when(alertGateway.findAlertById(anyString()))
                .thenReturn(Mono.just(alert));
        when(alertGateway.deleteAlert(any()))
                .thenReturn(Mono.just(alert.getId()));
        StepVerifier.create(useCase.deleteAlertRequest(alert.getId()))
                .expectNextCount(1)
                .verifyComplete();
        verify(alertGateway).deleteAlert(any());
    }

    @Test
    void updateAlertWithException() {
        when(alertGateway.updateAlert(any()))
                .thenReturn(Mono.empty());
        useCase.updateAlertRequest(alert)
                .as(StepVerifier::create)
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    void deleteAlertWithException() {
        when(alertGateway.findAlertById(anyString()))
                .thenReturn(Mono.empty());
        useCase.deleteAlertRequest(alert.getId())
                .as(StepVerifier::create)
                .expectError(BusinessException.class)
                .verify();
    }
}
