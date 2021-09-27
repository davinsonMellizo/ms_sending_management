package co.com.bancolombia.usecase.alertclient;

import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.config.model.alertclient.AlertClient;
import co.com.bancolombia.config.model.alertclient.gateways.AlertClientGateway;
import co.com.bancolombia.config.model.response.StatusResponse;
import org.assertj.core.api.Assertions;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AlertClientUseCaseTest {

    @Mock
    private AlertClientGateway alertClientGateway;

    @InjectMocks
    private AlertClientUseCase useCase;

    private final AlertClient alertClient = new AlertClient();

    @BeforeEach
    public void init() {
        alertClient.setIdAlert("1L");
    }

    @Test
    public void save() {
        when(alertClientGateway.save(any()))
                .thenReturn(Mono.just(alertClient));
        useCase.saveAlertClient(alertClient)
                .as(StepVerifier::create)
                .assertNext(response -> response
                        .getIdAlert().equals(alertClient.getIdAlert()))
                .verifyComplete();
        verify(alertClientGateway).save(any());
    }

    @Test
    public void update() {
        when(alertClientGateway.updateAlertClient(any()))
                .thenReturn(Mono.just(StatusResponse.<AlertClient>builder()
                        .actual(alertClient).before(alertClient).build()));
        when(alertClientGateway.findAlertClient(any()))
                .thenReturn(Mono.just(alertClient));
        useCase.updateAlertClient(alertClient)
                .as(StepVerifier::create)
                .assertNext(response -> response.getActual()
                        .getIdAlert().equals(alertClient.getIdAlert()))
                .verifyComplete();
        verify(alertClientGateway).updateAlertClient(any());
    }

    @Test
    public void findAll() {
        when(alertClientGateway.findAllAlertsByClient(any()))
                .thenReturn(Flux.just(alertClient));
        useCase.findAllAlertClient(alertClient)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
        verify(alertClientGateway).findAllAlertsByClient(any());
    }

    @Test
    public void delete() {
        when(alertClientGateway.delete(any()))
                .thenReturn(Mono.just("1"));
        useCase.deleteAlertClient(alertClient)
                .as(StepVerifier::create)
                .assertNext(str ->
                        Assertions.assertThat(str)
                                .isExactlyInstanceOf(String.class))
                .verifyComplete();
        verify(alertClientGateway).delete(any());
    }

    @Test
    public void updateThrowingException() {
        when(alertClientGateway.findAlertClient(any()))
                .thenReturn(Mono.empty());
        useCase.updateAlertClient(alertClient)
                .as(StepVerifier::create)
                .expectError(BusinessException.class)
                .verify();
    }

}
