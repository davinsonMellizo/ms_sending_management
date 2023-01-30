package co.com.bancolombia.usecase.sendalert;

import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.alert.gateways.AlertGateway;
import co.com.bancolombia.model.client.Client;
import co.com.bancolombia.model.client.Enrol;
import co.com.bancolombia.model.consumer.Consumer;
import co.com.bancolombia.model.consumer.gateways.ConsumerGateway;
import co.com.bancolombia.model.contact.Contact;
import co.com.bancolombia.model.contact.gateways.ContactGateway;
import co.com.bancolombia.model.response.StatusResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SendAlertUseCaseTest {
    @InjectMocks
    private SendAlertUseCase useCase;
    @Mock
    private AlertGateway alertGateway;
    @Mock
    private ContactGateway contactGateway;
    @Mock
    private ConsumerGateway consumerGateway;

    private final Client client = new Client();
    private final Contact contact = new Contact();

    @BeforeEach
    public void init() {
        client.setDocumentNumber(1061772353L);
        client.setDocumentType("0");
        client.setIdState(1);
        client.setConsumerCode("SVP");
        contact.setDocumentNumber(1061772353L);
        contact.setDocumentType("0");
        contact.setStateContact("1");
        contact.setValue("3214855124891");
        contact.setContactWay("SMS");
    }

    @Test
    void sendAlertUpdate(){
        when(alertGateway.sendAlert(any()))
                .thenReturn(Mono.just(new Alert()));
        when(consumerGateway.findConsumerById(anyString()))
                .thenReturn(Mono.just(Consumer.builder().segment("Personas").build()));
        when(contactGateway.contactsByClientAndSegment(any(), anyString()))
                .thenReturn(Mono.just(List.of(contact)));
        StatusResponse<Enrol> response = new StatusResponse<>();
        response.setActual(new Enrol(client, List.of(contact)));
        response.setBefore(new Enrol(client, List.of(contact)));
        StepVerifier
                .create(useCase.sendAlerts(new Enrol(client, List.of(contact)), response, false))
                .expectNextCount(1)
                .verifyComplete();
    }


}
