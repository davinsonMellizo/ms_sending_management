package co.com.bancolombia.usecase.client;

import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.client.Client;
import co.com.bancolombia.model.client.Enrol;
import co.com.bancolombia.model.client.ResponseUpdateClient;
import co.com.bancolombia.model.client.gateways.ClientGateway;
import co.com.bancolombia.model.client.gateways.ClientRepository;
import co.com.bancolombia.model.consumer.Consumer;
import co.com.bancolombia.model.consumer.gateways.ConsumerGateway;
import co.com.bancolombia.model.contact.Contact;
import co.com.bancolombia.model.contact.gateways.ContactGateway;
import co.com.bancolombia.model.contactmedium.ContactMedium;
import co.com.bancolombia.model.contactmedium.gateways.ContactMediumGateway;
import co.com.bancolombia.model.document.Document;
import co.com.bancolombia.model.document.gateways.DocumentGateway;
import co.com.bancolombia.model.events.gateways.CommandGateway;
import co.com.bancolombia.model.response.StatusResponse;
import co.com.bancolombia.model.state.State;
import co.com.bancolombia.model.state.gateways.StateGateway;
import co.com.bancolombia.usecase.commons.ValidateContact;
import co.com.bancolombia.usecase.contact.ContactUseCase;
import co.com.bancolombia.usecase.log.NewnessUseCase;
import co.com.bancolombia.usecase.sendalert.SendAlertUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static co.com.bancolombia.commons.enums.State.ACTIVE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientUseCaseTest {

    @InjectMocks
    private ClientUseCase useCase;
    @Mock
    private ContactUseCase contactUseCase;

    @Mock
    private ClientRepository clientRepository;
    @Mock
    private ClientGateway clientGateway;
    @Mock
    private NewnessUseCase newnessUseCase;
    @Mock
    private DocumentGateway documentGateway;
    @Mock
    private ContactGateway contactGateway;
    @Mock
    private StateGateway stateGateway;
    @Mock
    private ContactMediumGateway mediumGateway;
    @Mock
    private ConsumerGateway consumerGateway;
    @Mock
    private CommandGateway commandGateway;
    @Mock
    private SendAlertUseCase sendAlertUseCase;

    private final Client client = new Client();
    private final ResponseUpdateClient response = new ResponseUpdateClient();
    private final Contact contact = new Contact();
    private final Document document = new Document();
    private final State state = new State(0, "Active");
    private final ContactMedium medium = new ContactMedium(1, "Mail");
    private final Consumer consumer = new Consumer();

    @BeforeEach
    public void init() {
        client.setDocumentNumber(1061772353L);
        client.setDocumentType("CC");
        client.setId(0);
        client.setIdState(ACTIVE.getType());
        client.setStateClient("Activo");
        client.setVoucher("123456");
        client.setCreationUser("user");
        client.setConsumerCode("SVP");
        document.setId("0");

        contact.setContactWay("SMS");
        contact.setSegment("SVP");
        contact.setDocumentNumber(1061772353L);
        contact.setDocumentType("CC");
        contact.setValue("(+57)3217958455");
        contact.setStateContact("Active");
        contact.setId(1);
        contact.setPrevious(false);

        consumer.setSegment("Personas");
    }

    @Test
    void inactivateClient() {
        when(newnessUseCase.saveNewness((Client) any(), anyString(), anyString()))
                .thenReturn(Mono.just(client));
        when(clientRepository.findClientByIdentification(any()))
                .thenReturn(Mono.just(client));
        when(clientRepository.inactivateClient(any()))
                .thenReturn(Mono.just(client));

        StepVerifier
                .create(useCase.inactivateClient(client))
                .expectNextCount(1)
                .verifyComplete();

        verify(clientRepository).inactivateClient(client);
    }

    @Test
    void inactivateClientWithClientInactive(){
        client.setIdState(0);
        when(clientRepository.findClientByIdentification(any()))
                .thenReturn(Mono.empty());
        useCase.inactivateClient(client).as(StepVerifier::create)
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    void findClientByDocument() {
        when(clientRepository.findClientByIdentification(any()))
                .thenReturn(Mono.just(client));
        StepVerifier
                .create(useCase.findClientByIdentification(client))
                .expectNextCount(1)
                .verifyComplete();
        verify(clientRepository).findClientByIdentification(client);
    }

    @Test
    void saveClient() {
        when(newnessUseCase.saveNewness((Client) any(), anyString(), anyString()))
                .thenReturn(Mono.just(client));
        when(clientRepository.saveClient(any()))
                .thenReturn(Mono.just(client));
        when(documentGateway.getDocument(anyString()))
                .thenReturn(Mono.just(document));
        when(clientRepository.findClientByIdentification(any()))
                .thenReturn(Mono.empty());
        when(consumerGateway.findConsumerById(anyString()))
                .thenReturn(Mono.just(consumer));
        when(documentGateway.getDocument(anyString()))
                .thenReturn(Mono.just(document));
        when(contactUseCase.saveContact(any(), anyString()))
                .thenReturn(Mono.just(contact));
        when(contactUseCase.validateContacts(any()))
                .thenReturn(Mono.just(Enrol.builder().client(client).contactData(List.of(contact)).build()));
        when(commandGateway.sendCommandEnroll(any())).thenReturn(Mono.empty());
        when(stateGateway.findState(any()))
                .thenReturn(Mono.just(state));
        when(documentGateway.getDocument(anyString()))
                .thenReturn(Mono.just(document));
        when(consumerGateway.findConsumerById(anyString()))
                .thenReturn(Mono.just(consumer));
        when(sendAlertUseCase.sendAlerts(any(), any(), anyBoolean()))
                .thenReturn(Mono.just(new StatusResponse<>()));
        StepVerifier
                .create(useCase.saveClient(Enrol.builder().client(client)
                        .contactData(List.of(contact)).build(), false, "1123333", false))
                .expectNextCount(1)
                .verifyComplete();
        verify(clientRepository).saveClient(any());
    }

    @Test
    void updateClient() {

        when(stateGateway.findState(any()))
                .thenReturn(Mono.just(state));
        when(clientRepository.findClientByIdentification(any()))
                .thenReturn(Mono.just(client));
        when(documentGateway.getDocument(anyString()))
                .thenReturn(Mono.just(document));
        when(consumerGateway.findConsumerById(anyString()))
                .thenReturn(Mono.just(consumer));
        when(contactUseCase.updateContactRequest(any(), anyString()))
                .thenReturn(Mono.just(StatusResponse.<Contact>builder().before(contact)
                        .before(contact).build()));
        when(clientRepository.updateClient(any()))
                .thenReturn(Mono.just(StatusResponse.<Client>builder()
                        .before(client).actual(client)
                        .build()));
        when(newnessUseCase.saveNewness((Client) any(), anyString(), anyString()))
                .thenReturn(Mono.just(client));
        when(commandGateway.sendCommandUpdate(any())).thenReturn(Mono.empty());
        when(sendAlertUseCase.sendAlerts(any(), any(), anyBoolean()))
                .thenReturn(Mono.just(new StatusResponse<>()));
        StepVerifier
                .create(useCase.updateClientMcd(Enrol.builder().contactData(List.of(contact)).client(client).build(), true))
                .expectNextCount(1)
                .verifyComplete();
        verify(clientRepository).updateClient(any());
    }

    @Test
    void updateClientMono() {
        when(stateGateway.findState(any()))
                .thenReturn(Mono.just(state));
        when(clientRepository.findClientByIdentification(any()))
                .thenReturn(Mono.just(client));
        when(documentGateway.getDocument(anyString()))
                .thenReturn(Mono.just(document));
        when(consumerGateway.findConsumerById(anyString()))
                .thenReturn(Mono.just(consumer));
        when(contactUseCase.updateContactRequest(any(), anyString()))
                .thenReturn(Mono.just(StatusResponse.<Contact>builder().before(contact)
                        .before(contact).build()));
        when(clientRepository.updateClient(any()))
                .thenReturn(Mono.just(StatusResponse.<Client>builder()
                        .before(client).actual(client)
                        .build()));
        when(newnessUseCase.saveNewness((Client) any(), anyString(), anyString()))
                .thenReturn(Mono.just(client));
        when(commandGateway.sendCommandUpdate(any())).thenReturn(Mono.empty());
        when(sendAlertUseCase.sendAlerts(any(), any(), anyBoolean()))
                .thenReturn(Mono.just(new StatusResponse<>()));
        StepVerifier
                .create(useCase.updateClient(Enrol.builder().contactData(List.of(contact))
                        .client(client).build(), false, "123456", false))
                .expectNextCount(1)
                .verifyComplete();
        verify(clientRepository).updateClient(any());

    }

    @Test
    void findClientByDocumentWithException() {
        when(clientRepository.findClientByIdentification(any()))
                .thenReturn(Mono.empty());
        useCase.findClientByIdentification(client)
                .as(StepVerifier::create)
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    void deleteClient() {
        when(clientRepository.deleteClient(anyLong(), anyLong()))
                .thenReturn(Mono.just(1));
        StepVerifier
                .create(useCase.deleteClient(123L, 125L))
                .expectNextCount(1)
                .verifyComplete();
        verify(clientRepository).deleteClient(anyLong(), anyLong());
    }

}