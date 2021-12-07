package co.com.bancolombia.usecase.sendalert.validations;

import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.consumer.Consumer;
import co.com.bancolombia.model.contact.Contact;
import co.com.bancolombia.model.contact.gateways.ContactGateway;
import co.com.bancolombia.model.events.gateways.CommandGateway;
import co.com.bancolombia.model.message.Message;
import co.com.bancolombia.model.message.Parameter;
import co.com.bancolombia.model.provider.Provider;
import co.com.bancolombia.model.provider.gateways.ProviderGateway;
import co.com.bancolombia.model.remitter.Remitter;
import co.com.bancolombia.model.remitter.gateways.RemitterGateway;
import co.com.bancolombia.usecase.log.LogUseCase;
import co.com.bancolombia.usecase.sendalert.RouterProviderMailUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ValidateContactUseCaseTest {
    @InjectMocks
    private ValidateContactUseCase validateContactUseCase;
    @Mock
    private ContactGateway contactGateway;
    @Mock
    private LogUseCase logUseCase;

    private Message message = new Message();

    @BeforeEach
    public void init(){
        message.setOperation(1);
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
        ArrayList<Parameter> parameters = new ArrayList<>();
        Parameter parameter = Parameter.builder().Name("name").Value("bancolombia").build();
        parameters.add(parameter);
        message.setParameters(parameters);
    }


    @Test
    public void validateContactsTest(){
        Contact contactSms = Contact.builder().contactMedium("SMS").value("").build();
        Contact contactPush = Contact.builder().contactMedium("PUSH").value("").idState(1).build();
        Contact contactEmail = Contact.builder().contactMedium("MAIL").value("").build();
        when(contactGateway.findAllContactsByClient(any())).thenReturn(Flux.just(contactSms,contactEmail,contactPush));
        StepVerifier.create(validateContactUseCase.validateDataContact(message, Consumer.builder().segment("").build()))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void validateContactsErrorTest(){
        when(logUseCase.sendLogError(any(), anyString(), any())).thenReturn(Mono.empty());
        when(contactGateway.findAllContactsByClient(any())).thenReturn(Flux.empty());
        StepVerifier.create(validateContactUseCase.validateDataContact(message, Consumer.builder().segment("").build()))
                .verifyComplete();
    }

}
