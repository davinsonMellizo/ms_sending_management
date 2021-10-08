package co.com.bancolombia.usecase.contact;

import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.client.Client;
import co.com.bancolombia.model.client.gateways.ClientRepository;
import co.com.bancolombia.model.consumer.Consumer;
import co.com.bancolombia.model.consumer.gateways.ConsumerGateway;
import co.com.bancolombia.model.contact.Contact;
import co.com.bancolombia.model.contact.gateways.ContactGateway;
import co.com.bancolombia.model.contactmedium.ContactMedium;
import co.com.bancolombia.model.contactmedium.gateways.ContactMediumGateway;
import co.com.bancolombia.model.document.Document;
import co.com.bancolombia.model.document.gateways.DocumentGateway;
import co.com.bancolombia.model.state.State;
import co.com.bancolombia.model.state.gateways.StateGateway;
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
public class ContactUseCaseTest {

    @InjectMocks
    private ContactUseCase useCase;

    @Mock
    private ContactGateway contactGateway;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private StateGateway stateGateway;
    @Mock
    private ContactMediumGateway mediumGateway;
    @Mock
    private DocumentGateway documentGateway;
    @Mock
    private ConsumerGateway consumerGateway;

    private final State state = new State(0, "Active");
    private final ContactMedium medium = new ContactMedium(1, "Mail");
    private final Client client = new Client();
    private final Contact contact = new Contact();
    private final Document document = new Document();
    private final Consumer consumer = new Consumer();

    @BeforeEach
    public void init() {
        contact.setContactMedium("1");
        contact.setSegment("0");
        contact.setDocumentNumber(new Long(1061772353));
        contact.setDocumentType("0");
        contact.setValue("correo@gamail.com");
        contact.setState("Active");
        contact.setId(1);
        contact.setPrevious(false);

        client.setDocumentNumber(new Long(1061772353));
        client.setDocumentType("0");
        client.setIdState(0);

        consumer.setSegment("GNR");
        document.setId("0");

    }

    @Test
    public void findAllContactByClient() {
        when(contactGateway.contactsByClient(any()))
                .thenReturn(Flux.just(contact));
        when(clientRepository.findClientByIdentification(any()))
                .thenReturn(Mono.just(client));
        StepVerifier
                .create(useCase.findContactsByClient(client, ""))
                .expectNextCount(1)
                .verifyComplete();
        verify(contactGateway).contactsByClient(client);
    }

    @Test
    public void saveContact() {
        when(contactGateway.saveContact(any()))
                .thenReturn(Mono.just(contact));
        when(stateGateway.findState(any()))
                .thenReturn(Mono.just(state));
        when(mediumGateway.findContactMediumByCode(any()))
                .thenReturn(Mono.just(medium));
        when(documentGateway.getDocument(anyString()))
                .thenReturn(Mono.just(document));
        when(consumerGateway.findConsumerById(anyString()))
                .thenReturn(Mono.just(consumer));
        StepVerifier
                .create(useCase.saveContact(contact))
                .assertNext(response -> response
                        .getDocumentNumber()
                        .equals(contact.getDocumentNumber()))
                .verifyComplete();
        verify(contactGateway).saveContact(any());
        verify(mediumGateway).findContactMediumByCode(any());
    }


    public void updateContact() {
        when(clientRepository.findClientByIdentification(any()))
                .thenReturn(Mono.just(client));
        when(contactGateway.updateContact(any()))
                .thenReturn(Mono.just(contact));
        when(stateGateway.findState(any()))
                .thenReturn(Mono.just(state));
        when(contactGateway.findIdContact(any()))
                .thenReturn(Flux.just(contact));
        StepVerifier
                .create(useCase.updateContactRequest(contact))
                .assertNext(response -> response
                        .getActual().getDocumentNumber()
                        .equals(contact.getDocumentNumber()))
                .verifyComplete();
        verify(contactGateway).updateContact(any());
        verify(stateGateway).findState(any());
    }

    @Test
    public void deleteContact() {
        when(contactGateway.findIdContact(any()))
                .thenReturn(Flux.just(contact));
        when(contactGateway.deleteContact(any()))
                .thenReturn(Mono.just(1));
        StepVerifier.create(useCase.deleteContact(contact))
                .expectNextCount(1)
                .verifyComplete();
        verify(contactGateway).findIdContact(contact);
        verify(contactGateway).deleteContact(any());
    }

    @Test
    public void updateContactWithException() {
        when(clientRepository.findClientByIdentification(any()))
                .thenReturn(Mono.empty());
        useCase.updateContactRequest(contact)
                .as(StepVerifier::create)
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    public void deleteContactWithException() {
        when(contactGateway.findIdContact(any()))
                .thenReturn(Flux.empty());
        useCase.deleteContact(contact)
                .as(StepVerifier::create)
                .expectError(BusinessException.class)
                .verify();
    }
}
