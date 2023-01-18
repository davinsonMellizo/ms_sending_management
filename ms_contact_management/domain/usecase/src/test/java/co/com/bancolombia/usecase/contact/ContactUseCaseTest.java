package co.com.bancolombia.usecase.contact;

import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.client.Client;
import co.com.bancolombia.model.client.Enrol;
import co.com.bancolombia.model.client.gateways.ClientGateway;
import co.com.bancolombia.model.client.gateways.ClientRepository;
import co.com.bancolombia.model.consumer.Consumer;
import co.com.bancolombia.model.consumer.gateways.ConsumerGateway;
import co.com.bancolombia.model.contact.Contact;
import co.com.bancolombia.model.contact.ResponseContacts;
import co.com.bancolombia.model.contact.gateways.ContactGateway;
import co.com.bancolombia.model.contactmedium.ContactMedium;
import co.com.bancolombia.model.contactmedium.gateways.ContactMediumGateway;
import co.com.bancolombia.model.document.Document;
import co.com.bancolombia.model.document.gateways.DocumentGateway;
import co.com.bancolombia.model.state.State;
import co.com.bancolombia.model.state.gateways.StateGateway;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContactUseCaseTest {

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
    @Mock
    private NewnessUseCase newnessUseCase;
    @Mock
    private ClientGateway clientGateway;

    private final State state = new State(0, "Active");
    private final ContactMedium medium = new ContactMedium(1, "Mail");
    private final Client client = new Client();
    private final Contact contact = new Contact();
    private final Document document = new Document();
    private final Consumer consumer = new Consumer();
    private final Enrol enrol = new Enrol();

    @BeforeEach
    public void init() {
        contact.setContactWay("1");
        contact.setSegment("GNR");
        contact.setDocumentNumber(1061772353L);
        contact.setDocumentType("0");
        contact.setValue("321795845");
        contact.setStateContact("Active");
        contact.setId(1);
        contact.setContactWayName("SMS");

        client.setDocumentNumber(1061772353L);
        client.setDocumentType("0");
        client.setIdState(1);
        client.setConsumerCode("SVP");

        consumer.setSegment("GNR");
        document.setId("0");
        enrol.setClient(client);
    }

    @Test
    void findAllContactCloudWithoutChanel() {
        when(contactGateway.contactsByClient(any()))
                .thenReturn(Mono.just(List.of(contact)));
        when(clientRepository.findClientByIdentification(any()))
                .thenReturn(Mono.just(client));
        StepVerifier
                .create(useCase.findContactsByClient(client, ""))
                .expectNextCount(1)
                .verifyComplete();
        verify(contactGateway).contactsByClient(client);
    }

    @Test
    void findAllContactCloudWithChanel() {
        when(contactGateway.contactsByClient(any()))
                .thenReturn(Mono.just(List.of(contact)));
        when(consumerGateway.findConsumerById(anyString()))
                .thenReturn(Mono.just(consumer));
        when(contactGateway.contactsByClientAndSegment(any(), anyString()))
                .thenReturn(Mono.just(List.of(contact)));
        when(clientRepository.findClientByIdentification(any()))
                .thenReturn(Mono.just(client));
        when(documentGateway.getDocument(any()))
                .thenReturn(Mono.just(document));
        StepVerifier
                .create(useCase.findContactsByClient(client, "GNR"))
                .expectNextCount(1)
                .verifyComplete();
        verify(contactGateway).contactsByClient(client);
    }

    @Test
    void findAllContactIseriesdWithoutChanel() {
        when(documentGateway.getDocument(any()))
                .thenReturn(Mono.just(document));
        when(clientGateway.retrieveAlertInformation(any()))
                .thenReturn(Mono.just(ResponseContacts.builder().contacts(List.of(contact)).build()));
        when(clientRepository.findClientByIdentification(any()))
                .thenReturn(Mono.empty());
        StepVerifier
                .create(useCase.findContactsByClient(client, ""))
                .expectNextCount(1)
                .verifyComplete();
        verify(clientGateway).retrieveAlertInformation(any());
    }

    @Test
    void findAllContactIseriesWithChanel() {
        when(clientGateway.retrieveAlertInformation(any()))
                .thenReturn(Mono.just(ResponseContacts.builder().contacts(List.of(contact)).build()));
        when(consumerGateway.findConsumerById(anyString()))
                .thenReturn(Mono.just(consumer));
        when(clientRepository.findClientByIdentification(any()))
                .thenReturn(Mono.empty());
        when(documentGateway.getDocument(any()))
                .thenReturn(Mono.just(document));
        StepVerifier
                .create(useCase.findContactsByClient(client, "GNR"))
                .expectNextCount(1)
                .verifyComplete();
    }


    @Test
    void saveContact() {
        when(newnessUseCase.saveNewness((Contact) any(), anyString(), anyString()))
                .thenReturn(Mono.just(contact));
        when(contactGateway.saveContact(any()))
                .thenReturn(Mono.just(contact));
        StepVerifier
                .create(useCase.saveContact(contact, "voucher"))
                .assertNext(response -> response
                        .getDocumentNumber()
                        .equals(contact.getDocumentNumber()))
                .verifyComplete();
        verify(contactGateway).saveContact(any());
    }

    @Test
    void updateContact() {
        contact.setPrevious(false);
        when(contactGateway.saveContact(any()))
                .thenReturn(Mono.just(contact));
        when(newnessUseCase.saveNewness((Contact) any(), anyString(), anyString()))
                .thenReturn(Mono.just(contact));
        when(consumerGateway.findConsumerById(anyString()))
                .thenReturn(Mono.just(consumer));
        when(clientRepository.findClientByIdentification(any()))
                .thenReturn(Mono.just(client));
        when(contactGateway.updateContact(any()))
                .thenReturn(Mono.just(contact));
        when(stateGateway.findState(any()))
                .thenReturn(Mono.just(state));
        when(contactGateway.findContactsByClientSegmentAndMedium(any()))
                .thenReturn(Flux.just(contact));

        StepVerifier
                .create(useCase.updateContactRequest(contact, "12345"))
                .assertNext(response -> response
                        .getActual().getDocumentNumber()
                        .equals(contact.getDocumentNumber()))
                .verifyComplete();
        verify(contactGateway).updateContact(any());
    }

    @Test
    void updateContactWithExistent() {
        contact.setPrevious(false);
        when(newnessUseCase.saveNewness((Contact) any(), anyString(), anyString()))
                .thenReturn(Mono.just(contact));
        when(consumerGateway.findConsumerById(anyString()))
                .thenReturn(Mono.just(consumer));
        when(clientRepository.findClientByIdentification(any()))
                .thenReturn(Mono.just(client));
        when(contactGateway.updateContact(any()))
                .thenReturn(Mono.just(contact));
        when(contactGateway.saveContact(any()))
                .thenReturn(Mono.just(contact));
        when(stateGateway.findState(any()))
                .thenReturn(Mono.just(state));
        when(contactGateway.findContactsByClientSegmentAndMedium(any()))
                .thenReturn(Flux.just(contact.toBuilder().value("69784585254").build(),
                        contact.toBuilder().previous(false).value("69784585254").build()));
        StepVerifier
                .create(useCase.updateContactRequest(contact, "123456"))
                .assertNext(response -> response
                        .getActual().getDocumentNumber()
                        .equals(contact.getDocumentNumber()))
                .verifyComplete();
        verify(contactGateway).updateContact(any());

    }



    @Test
    void validateContacts() {
        enrol.setContactData(List.of(contact));
        when(stateGateway.findState(any()))
                .thenReturn(Mono.just(state));
        when(mediumGateway.findContactMediumByCode(any()))
                .thenReturn(Mono.just(medium));
        when(consumerGateway.findConsumerById(anyString()))
                .thenReturn(Mono.just(consumer));

        StepVerifier
                .create(useCase.validateContacts(enrol))
                .assertNext(response -> response.getClient()
                        .getConsumerCode()
                        .equals(client.getConsumerCode()))
                .verifyComplete();
    }

    @Test
    void validatePhone() {
        contact.setContactWay("SMS");
        contact.setValue("3207288544");
        enrol.setContactData(List.of(contact));
        StepVerifier
                .create(useCase.validatePhone(enrol))
                .assertNext(response -> response.getClient()
                        .getConsumerCode()
                        .equals(client.getConsumerCode()))
                .verifyComplete();
    }

    @Test
    void validateMail() {
        contact.setContactWay("MAIL");
        contact.setValue("mail@mail.com");
        contact.setEnvironmentType("Personal");
        enrol.setContactData(List.of(contact));
        StepVerifier
                .create(useCase.validateMail(enrol))
                .assertNext(response -> response.getClient()
                        .getConsumerCode()
                        .equals(client.getConsumerCode()))
                .verifyComplete();
    }

    @Test
    void validatePhoneException() {
        contact.setContactWay("SMS");
        contact.setValue("1235");
        enrol.setContactData(List.of(contact));
        useCase.validatePhone(enrol)
                .as(StepVerifier::create)
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    void validateMailException() {
        contact.setContactWay("MAIL");
        contact.setValue("zzzzz");
        contact.setEnvironmentType("Personal");
        enrol.setContactData(List.of(contact));
        useCase.validateMail(enrol)
                .as(StepVerifier::create)
                .expectError(BusinessException.class)
                .verify();
    }

}
