package co.com.bancolombia.contact;

import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.contact.data.ContactMapper;
import co.com.bancolombia.contact.reader.ContactRepositoryReader;
import co.com.bancolombia.contact.writer.ContactRepository;
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
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class ContactRepositoryImplWithExceptionTest {

    private final Client client = new Client();
    private final Contact contact = new Contact();

    @InjectMocks
    private ContactRepositoryImplement repositoryImpl;
    @Mock
    private ContactRepositoryReader repositoryReader;@Mock
    private ContactRepository repository;
    @Spy
    private ContactMapper mapper = Mappers.getMapper(ContactMapper.class);

    @BeforeEach
    public void init() {
        contact.setContactWay("1");
        contact.setSegment("0");
        contact.setDocumentNumber(1061772353L);
        contact.setDocumentType("0");
        contact.setValue("correo@gamail.com");
        contact.setStateContact("0");

        client.setDocumentType("0");
        client.setDocumentNumber(1061772353L);
    }

    @Test
    void findAllContactsByClient() {
        when(repositoryReader.findAllContactsByClient(any(), any()))
                .thenReturn(Flux.error(RuntimeException::new));
        repositoryImpl.contactsByClient(client)
                .as(StepVerifier::create)
                .expectError(TechnicalException.class)
                .verify();
    }

    @Test
    void findIdContact() {
        when(repositoryReader.findContact(any(), any(), any(), any()))
                .thenReturn(Flux.error(RuntimeException::new));
        contact.setContactWay("SMS");
        contact.setSegment("ALM");
        repositoryImpl.findContactsByClientSegmentAndMedium(contact)
                .as(StepVerifier::create)
                .expectError(TechnicalException.class)
                .verify();
    }
}