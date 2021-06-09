package co.com.bancolombia.alert;

import co.com.bancolombia.model.alert.Alert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AlertRepositoryImplTest {

    @Autowired
    private AlertRepositoryImplement contactRepositoryImplement;
    private final Alert alert = new Alert();

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
        StepVerifier.create(contactRepositoryImplement.findAllContactsByClient(client))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void findIdContact() {
        alert.setContactMedium("SMS");
        alert.setEnrollmentContact("ALM");
        StepVerifier.create(contactRepositoryImplement.findIdContact(alert))
                .consumeNextWith(id -> assertEquals(1, id))
                .verifyComplete();
    }

    @Test
    public void updateContact() {
        alert.setContactMedium("SMS");
        alert.setEnrollmentContact("ALM");
        alert.setValue("3216931596");
        StepVerifier.create(contactRepositoryImplement.updateAlert(alert))
                .consumeNextWith(contactU -> assertEquals("3216931596", contactU.getValue()))
                .verifyComplete();
    }

    @Test
    public void saveContact() {
        contactRepositoryImplement.saveAlert(alert)
                .subscribe(contactSaved -> StepVerifier
                        .create(contactRepositoryImplement.findIdContact(contactSaved))
                        .expectNextCount(1)
                        .verifyComplete());
    }

    @Test
    public void deleteContact() {
        alert.setContactMedium("SMS");
        alert.setEnrollmentContact("ALM");
        contactRepositoryImplement.findIdContact(alert)
                .subscribe(id -> StepVerifier
                        .create(contactRepositoryImplement.deleteAlert(id))
                        .verifyComplete());
    }*/
}