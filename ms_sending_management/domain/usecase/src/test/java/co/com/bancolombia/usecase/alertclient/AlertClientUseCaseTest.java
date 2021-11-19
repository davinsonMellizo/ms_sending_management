package co.com.bancolombia.usecase.alertclient;

import co.com.bancolombia.model.alertclient.AlertClient;
import co.com.bancolombia.model.alertclient.gateways.AlertClientGateway;
import co.com.bancolombia.model.client.Client;
import co.com.bancolombia.model.client.gateways.ClientGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static co.com.bancolombia.commons.constants.Header.DOCUMENT_NUMBER;
import static co.com.bancolombia.commons.constants.Header.DOCUMENT_TYPE;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AlertClientUseCaseTest {

    @Mock
    private AlertClientGateway alertClientGateway;
    @Mock
    private ClientGateway clientGateway;

    @InjectMocks
    private AlertClientUseCase useCase;

    private final AlertClient alertClient = new AlertClient();

    @BeforeEach
    public void init() {
        alertClient.setIdAlert("1");
        alertClient.setDocumentNumber(1061481L);
        alertClient.setDocumentType(0);
        alertClient.setAmountEnable(558L);
        alertClient.setNumberOperations(0);
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
                .thenReturn(Mono.just(alertClient));
        when(alertClientGateway.save(any()))
                .thenReturn(Mono.just(alertClient));
        when(alertClientGateway.findAlertsByClient(anyLong(), anyInt()))
                .thenReturn(Flux.just(alertClient));
        useCase.updateAlertClient(List.of(alertClient))
                .as(StepVerifier::create)
                .assertNext(response -> response
                        .getIdAlert().equals(alertClient.getIdAlert()))
                .verifyComplete();
        verify(alertClientGateway).updateAlertClient(any());
        verify(alertClientGateway).findAlertsByClient(anyLong(), anyInt());
    }

    @Test
    public void findAll() {
        when(clientGateway.findClientByIdentification(anyLong(), anyInt()))
                .thenReturn(Mono.just(Client.builder()
                        .documentNumber(1061772353L)
                        .idDocumentType(0)
                        .build()));
        when(alertClientGateway.alertsVisibleChannelByClient(anyLong(), anyInt()))
                .thenReturn(Flux.just(alertClient));
        Map<String, String> headers = new HashMap<>();
        headers.put(DOCUMENT_NUMBER, "1061772353");
        headers.put(DOCUMENT_TYPE, "0");
        useCase.findAllAlertClientByClient(headers)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
        verify(alertClientGateway).alertsVisibleChannelByClient(anyLong(), anyInt());
    }

    @Test
    public void delete() {
        when(alertClientGateway.delete(any()))
                .thenReturn(Mono.just(alertClient));
        useCase.deleteAlertClient(alertClient)
                .as(StepVerifier::create)
                .assertNext(alertClientD -> alertClientD.getIdAlert().equals(alertClient.getIdAlert()))
                .verifyComplete();
        verify(alertClientGateway).delete(any());
    }


}
