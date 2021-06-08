package co.com.bancolombia.contact;


import co.com.bancolombia.model.client.Client;
import co.com.bancolombia.model.contact.Contact;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ContactRepositoryImplTest {

    @Autowired
    private ContactRepositoryImplement contactRepositoryImplement;
    private final Client client = new Client(new Long(1061772353), 0);
    private final Contact contact = new Contact();

    @BeforeEach
    public void init() {
        contact.setIdContactMedium(1);
        contact.setIdEnrollmentContact(0);
        contact.setDocumentNumber(new Long(1061772353));
        contact.setDocumentType(0);
        contact.setValue("correo@gamail.com");
        contact.setIdState(0);
    }

    @Test
    public void findAllContactsByClient() {
        StepVerifier.create(contactRepositoryImplement.findAllContactsByClient(client))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void findIdContact() {
        contact.setContactMedium("SMS");
        contact.setEnrollmentContact("ALM");
        StepVerifier.create(contactRepositoryImplement.findIdContact(contact))
                .consumeNextWith(id -> assertEquals(id, 1))
                .verifyComplete();
    }

    @Test
    public void updateContact() {
        contact.setContactMedium("SMS");
        contact.setEnrollmentContact("ALM");
        contact.setValue("3216931596");
        StepVerifier.create(contactRepositoryImplement.updateContact(contact))
                .consumeNextWith(contactU -> assertEquals(contactU.getValue(), "3216931596"))
                .verifyComplete();
    }

    @Test
    public void saveContact() {
        contactRepositoryImplement.saveContact(contact)
                .subscribe(contactSaved -> StepVerifier
                        .create(contactRepositoryImplement.findIdContact(contactSaved))
                        .expectNextCount(1)
                        .verifyComplete());
    }

    @Test
    public void deleteContact() {
        contact.setContactMedium("SMS");
        contact.setEnrollmentContact("ALM");
        contactRepositoryImplement.findIdContact(contact)
                .subscribe(id -> StepVerifier
                        .create(contactRepositoryImplement.deleteContact(id))
                        .verifyComplete());
    }
}