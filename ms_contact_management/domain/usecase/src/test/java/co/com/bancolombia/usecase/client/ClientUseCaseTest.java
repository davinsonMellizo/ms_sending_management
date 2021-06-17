package co.com.bancolombia.usecase.client;

import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.client.Client;
import co.com.bancolombia.model.client.gateways.ClientGateway;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClientUseCaseTest {

    @InjectMocks
    private ClientUseCase useCase;

    @Mock
    private ClientGateway clientGateway;
    private final Client client = new Client();

    @BeforeEach
    public void init() {
        client.setDocumentNumber(new Long(1061772353));
        client.setDocumentType(0);
    }

    @Test
    public void findClientByDocument() {
        when(clientGateway.findClientByIdentification(any()))
                .thenReturn(Mono.just(client));
        StepVerifier
                .create(useCase.findClientByIdentification(client))
                .expectNextCount(1)
                .verifyComplete();
        verify(clientGateway).findClientByIdentification(client);
    }

    @Test
    public void saveContact() {
        when(clientGateway.saveClient(any()))
                .thenReturn(Mono.just(client));
        StepVerifier
                .create(useCase.saveClient(client))
                .assertNext(response -> response
                        .getDocumentNumber()
                        .equals(client.getDocumentNumber()))
                .verifyComplete();
        verify(clientGateway).saveClient(any());
    }

    @Test
    public void updateContact() {
        when(clientGateway.updateClient(any()))
                .thenReturn(Mono.just(StatusResponse.<Client>builder()
                        .before(client).actual(client)
                        .build()));
        when(clientGateway.findClientByIdentification(any()))
                .thenReturn(Mono.just(client));
        StepVerifier
                .create(useCase.updateClient(client))
                .assertNext(response -> response
                        .getActual().getDocumentNumber()
                        .equals(client.getDocumentNumber()))
                .verifyComplete();
        verify(clientGateway).updateClient(any());
    }

    @Test
    public void deleteContact() {
        when(clientGateway.deleteClient(any()))
                .thenReturn(Mono.just(client));
        StepVerifier.create(useCase.deleteClient(client))
                .expectNextCount(1)
                .verifyComplete();
        verify(clientGateway).deleteClient(client);
    }

    @Test
    public void findClientByDocumentWithException() {
        when(clientGateway.findClientByIdentification(any()))
                .thenReturn(Mono.empty());
        useCase.findClientByIdentification(client)
                .as(StepVerifier::create)
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    public void updateContactWithException() {
        when(clientGateway.findClientByIdentification(any()))
                .thenReturn(Mono.empty());
        useCase.updateClient(client)
                .as(StepVerifier::create)
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    public void deleteContactWithException() {
        when(clientGateway.deleteClient(any()))
                .thenReturn(Mono.empty());
        useCase.deleteClient(client)
                .as(StepVerifier::create)
                .expectError(BusinessException.class)
                .verify();
    }
}
