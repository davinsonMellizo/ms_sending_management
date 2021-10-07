package co.com.bancolombia.usecase.client;

import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.client.Client;
import co.com.bancolombia.model.client.Enrol;
import co.com.bancolombia.model.client.gateways.ClientGateway;
import co.com.bancolombia.model.client.gateways.ClientRepository;
import co.com.bancolombia.model.contact.Contact;
import co.com.bancolombia.model.document.Document;
import co.com.bancolombia.model.document.gateways.DocumentGateway;
import co.com.bancolombia.model.response.StatusResponse;
import co.com.bancolombia.usecase.contact.ContactUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClientUseCaseTest {

    @InjectMocks
    private ClientUseCase useCase;
    @Mock
    private ContactUseCase contactUseCase;

    @Mock
    private ClientRepository clientRepository;
    @Mock
    private ClientGateway clientGateway;
    @Mock
    private DocumentGateway documentGateway;
    private final Client client = new Client();
    private final Document document = new Document();

    @BeforeEach
    public void init() {
        client.setDocumentNumber(new Long(1061772353));
        client.setDocumentType("0");
        client.setId(0);
        client.setIdState(0);
        document.setId("0");
    }

    @Test
    public void findClientByDocument() {
        when(clientRepository.findClientByIdentification(any()))
                .thenReturn(Mono.just(client));
        StepVerifier
                .create(useCase.findClientByIdentification(client))
                .expectNextCount(1)
                .verifyComplete();
        verify(clientRepository).findClientByIdentification(client);
    }

    @Test
    public void saveClient() {
        when(clientRepository.saveClient(any()))
                .thenReturn(Mono.just(client));
        when(documentGateway.getDocument(anyString()))
                .thenReturn(Mono.just(document));
        when(clientRepository.findClientByIdentification(any()))
                .thenReturn(Mono.empty());
        when(clientGateway.matchClientWithBasicKit(any()))
                .thenReturn(Mono.just(true));
        StepVerifier
                .create(useCase.saveClient(Enrol.builder().client(client).contacts(new ArrayList<>()).build()))
                .assertNext(response -> response
                        .getDocumentNumber()
                        .equals(client.getDocumentNumber()))
                .verifyComplete();
        verify(clientRepository).saveClient(any());
    }

    @Test
    public void updateClient() {
        when(clientRepository.findClientByIdentification(any()))
                .thenReturn(Mono.just(client));
        when(documentGateway.getDocument(anyString()))
                .thenReturn(Mono.just(document));
        when(clientRepository.updateClient(any()))
                .thenReturn(Mono.just(StatusResponse.<Client>builder()
                        .before(client).actual(client)
                        .build()));
        Contact contact = new Contact();
        ArrayList<Contact> contacts = new ArrayList();
        contacts.add(contact);
        when(contactUseCase.updateContactRequest(any()))
                .thenReturn(Mono.just(StatusResponse.<Contact>builder().before(contact).before(contact).build()));
        StepVerifier
                .create(useCase.updateClient(Enrol.builder().contacts(contacts).client(client).build()))
                .assertNext(response -> response
                        .getActual().getClient().getDocumentNumber()
                        .equals(client.getDocumentNumber()))
                .verifyComplete();
        verify(clientRepository).updateClient(any());
    }

    @Test
    public void deleteClient() {
        when(clientRepository.deleteClient(any()))
                .thenReturn(Mono.just(client));
        when(documentGateway.getDocument(anyString()))
                .thenReturn(Mono.just(document));
        StepVerifier.create(useCase.deleteClient(client))
                .expectNextCount(1)
                .verifyComplete();
        verify(clientRepository).deleteClient(client);
    }

    @Test
    public void findClientByDocumentWithException() {
        when(clientRepository.findClientByIdentification(any()))
                .thenReturn(Mono.empty());
        useCase.findClientByIdentification(client)
                .as(StepVerifier::create)
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    public void updateContactWithException() {
        when(clientRepository.findClientByIdentification(any()))
                .thenReturn(Mono.empty());
        useCase.updateClient(Enrol.builder().client(client).build())
                .as(StepVerifier::create)
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    public void deleteContactWithException() {
        when(clientRepository.deleteClient(any()))
                .thenReturn(Mono.empty());
        when(documentGateway.getDocument(anyString()))
                .thenReturn(Mono.just(document));
        useCase.deleteClient(client)
                .as(StepVerifier::create)
                .expectError(BusinessException.class)
                .verify();
    }
}
