package co.com.bancolombia.usecase.sendalert.routers;

import co.com.bancolombia.model.client.Client;
import co.com.bancolombia.model.client.gateways.ClientGateway;
import co.com.bancolombia.model.contact.Contact;
import co.com.bancolombia.model.contact.gateways.ContactGateway;
import co.com.bancolombia.model.message.Message;
import co.com.bancolombia.usecase.sendalert.ClientUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientUseCaseTest {
    @InjectMocks
    private ClientUseCase clientUseCase;
    @Mock
    private ContactGateway contactGateway;
    @Mock
    private ClientGateway clientGateway;

    private Client client;
    private final Message message = new Message();

    @BeforeEach
    public void init(){
        message.setRetrieveInformation(true);
        message.setDocumentType(0);
        message.setDocumentNumber(1061781558L);
        message.setConsumer("SVP");
        message.setAlert("AFI");
        message.setTransactionCode("580");
        message.setAmount(60000L);
        message.setUrl("");
        message.setPhone("32158967");
        message.setPhoneIndicator("57");
        message.setMail("bancolombia@com.co");
        message.setAttachments(new ArrayList<>());
        Map<String, String> parameters = new HashMap<>();
        parameters.put("name", "bancolombia");
        message.setParameters(parameters);

        client = Client.builder()
                .idState(1)
                .build();

    }

    @Test
    void validateClientTest(){
        Contact contactSms = Contact.builder().contactMedium("SMS").value("").idState(1).previous(false)
                .stateClient(1).documentNumber(10000000000L).documentType(1).contactMedium("SVP")
                .build();
        Contact contactPush = Contact.builder().contactMedium("PUSH").value("").idState(1).previous(false)
                .stateClient(1).documentNumber(10000000000L).documentType(1).contactMedium("SVP")
                .build();
        Contact contactEmail = Contact.builder().contactMedium("MAIL").value("").idState(1).previous(false)
                .stateClient(1).documentNumber(10000000000L).documentType(1).contactMedium("SVP")
                .build();
        when(contactGateway.findAllContactsByClient(any())).thenReturn(Flux.just(contactSms,contactEmail,contactPush));

        StepVerifier.create(clientUseCase.validateClientInformation(message))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void validateContactsErrorTest(){
        when(contactGateway.findAllContactsByClient(any())).thenReturn(Flux.empty());
        StepVerifier.create(clientUseCase.validateClientInformation(message))
                .expectError().verify();
    }

}
