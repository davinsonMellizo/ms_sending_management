package co.com.bancolombia.contact;

import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.contact.data.ContactMapper;
import co.com.bancolombia.model.client.Client;
import co.com.bancolombia.model.contact.Contact;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ContactRepositoryImplWithExceptionTest {

    private final Client client = new Client();
    private final Contact contact = new Contact();

    @InjectMocks
    private ContactRepositoryImplement repositoryImpl;
    @Mock
    private ContactRepository repository;
    @Spy
    private ContactMapper mapper = Mappers.getMapper(ContactMapper.class);

    @BeforeEach
    public void init() {
        contact.setContactMedium("1");
        contact.setConsumer("0");
        contact.setDocumentNumber(new Long(1061772353));
        contact.setDocumentType("0");
        contact.setValue("correo@gamail.com");
        contact.setState("0");

        client.setDocumentType("0");
        client.setDocumentNumber(new Long(1061772353));
    }

    @Test
    public void findAllContactsByClient() {
        when(repository.findAllContactsByClient(any(), any()))
                .thenReturn(Flux.error(RuntimeException::new));
        repositoryImpl.findAllContactsByClient(client)
                .as(StepVerifier::create)
                .expectError(TechnicalException.class)
                .verify();
    }

    @Test
    public void findIdContact() {
        when(repository.findContact(any(), any(), any(), any()))
                .thenReturn(Mono.error(RuntimeException::new));
        contact.setContactMedium("SMS");
        contact.setConsumer("ALM");
        repositoryImpl.findIdContact(contact)
                .as(StepVerifier::create)
                .expectError(TechnicalException.class)
                .verify();
    }
}