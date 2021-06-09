package co.com.bancolombia.alert;

import co.com.bancolombia.alert.data.AlertMapper;
import co.com.bancolombia.model.alert.Alert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class AlertRepositoryImplWithExceptionTest {

    private final Alert alert = new Alert();

    @InjectMocks
    private AlertRepositoryImplement repositoryImpl;
    @Mock
    private AlertRepository repository;
    @Spy
    private AlertMapper mapper = Mappers.getMapper(AlertMapper.class);

    @BeforeEach
    public void init() {
        alert.setIdContactMedium(1);
        alert.setIdEnrollmentContact(0);
        alert.setDocumentNumber(new Long(1061772353));
        alert.setDocumentType(0);
        alert.setValue("correo@gamail.com");
        alert.setIdState(0);
    }

   /* @Test
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
        alert.setContactMedium("SMS");
        alert.setEnrollmentContact("ALM");
        repositoryImpl.findIdContact(alert)
                .as(StepVerifier::create)
                .expectError(TechnicalException.class)
                .verify();
    }*/
}