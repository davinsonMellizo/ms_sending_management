package co.com.bancolombia.usecase.alertclient;

import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.alert.gateways.AlertGateway;
import co.com.bancolombia.model.alertclient.AlertClient;
import co.com.bancolombia.model.alertclient.gateways.AlertClientGateway;
import co.com.bancolombia.model.client.Client;
import co.com.bancolombia.model.client.ResponseClient;
import co.com.bancolombia.model.client.gateways.ClientGateway;
import co.com.bancolombia.usecase.log.NewnessUseCase;
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

import static co.com.bancolombia.commons.constants.Header.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AlertClientUseCaseTest {

    @Mock
    private AlertClientGateway alertClientGateway;
    @Mock
    private ClientGateway clientGateway;
    @Mock
    private AlertGateway alertGateway;

    @Mock
    private NewnessUseCase newnessUseCase;

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
    void save() {
        when(alertClientGateway.save(any()))
                .thenReturn(Mono.just(alertClient));
        when(newnessUseCase.saveNewness(any(), any()))
                .thenReturn(Mono.just(alertClient));
        useCase.saveAlertClient(List.of(alertClient))
                .as(StepVerifier::create)
                .assertNext(response -> assertEquals(response.getIdAlert(), alertClient.getIdAlert()))
                .verifyComplete();
        verify(alertClientGateway).save(any());
    }

    @Test
    void basicKit() {
        Alert alert = new Alert();
        alert.setDescription("");
        alert.setId("AL");
        alert.setNature("MO");
        Map<String, String> headers = new HashMap<>();
        headers.put(DOCUMENT_NUMBER, "1");
        headers.put(DOCUMENT_TYPE, "1");
        headers.put(ASSOCIATION_ORIGIN, "ALM");
        when(alertClientGateway.save(any()))
                .thenReturn(Mono.just(alertClient));
        when(newnessUseCase.saveNewness(any(), any()))
                .thenReturn(Mono.just(alertClient));
        when(useCase.saveAlertClient(any()))
                .thenReturn(Mono.just(alertClient));
        when(alertGateway.findAlertKitBasic())
                .thenReturn(Flux.just(alert));
        useCase.matchClientWithBasicKit(headers)
                .as(StepVerifier::create)
                .assertNext(ResponseClient::getResponse)
                .verifyComplete();
    }

    @Test
    void update() {
        when(newnessUseCase.saveNewness(any(), any()))
                .thenReturn(Mono.just(alertClient));
        when(alertClientGateway.updateAlertClient(any()))
                .thenReturn(Mono.just(alertClient));
        when(alertClientGateway.save(any()))
                .thenReturn(Mono.just(alertClient));
        when(alertClientGateway.findAlertsByClient(anyLong(), anyInt()))
                .thenReturn(Flux.just(alertClient));
        useCase.updateAlertClient(List.of(alertClient))
                .as(StepVerifier::create)
                .assertNext(response -> assertEquals(response.getIdAlert(), alertClient.getIdAlert()))
                .verifyComplete();
        verify(alertClientGateway).updateAlertClient(any());
        verify(alertClientGateway).findAlertsByClient(anyLong(), anyInt());
    }

    @Test
    void findAll() {
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
    void delete() {
        when(alertClientGateway.delete(any()))
                .thenReturn(Mono.just(alertClient));
        when(alertClientGateway.findAlertClient(any()))
                .thenReturn(Mono.just(alertClient));
        when(newnessUseCase.saveNewness(any(), any()))
                .thenReturn(Mono.just(alertClient));
        useCase.deleteAlertClient(alertClient)
                .as(StepVerifier::create)
                .assertNext(alertClientD -> assertEquals(alertClientD.getIdAlert(), alertClient.getIdAlert()))
                .verifyComplete();
        verify(alertClientGateway).delete(any());
    }


}
