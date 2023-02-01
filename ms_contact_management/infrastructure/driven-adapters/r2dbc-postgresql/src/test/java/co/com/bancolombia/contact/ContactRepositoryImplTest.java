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

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ContactRepositoryImplTest {

    @Autowired
    private ContactRepositoryImplement contactRepositoryImplement;

    private final Client client = new Client();
    private final Contact contact = new Contact();

    @BeforeEach
    public void init() {
        contact.setContactWay("1");
        contact.setSegment("ALM");
        contact.setDocumentNumber(1061772353L);
        contact.setDocumentType("0");
        contact.setValue("correo@gamail.com");
        contact.setStateContact("0");
        contact.setCreatedDate(LocalDateTime.now());
        contact.setPrevious(false);
        contact.setId(0);

        client.setDocumentNumber(new Long(1061772353));
        client.setDocumentType("0");
    }

    @Test
    void findAllContactsByClient() {
        StepVerifier.create(contactRepositoryImplement.contactsByClient(client))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void findIdContact() {
        contact.setContactWay("SMS");
        contact.setSegment("ALM");
        StepVerifier.create(contactRepositoryImplement.findContactsByClientSegmentAndMedium(contact))
                .consumeNextWith(contact -> assertEquals(0, contact.getId()))
                .verifyComplete();
    }

    @Test
    void updateContact() {
        contact.setContactWay("0");
        contact.setSegment("ALM");
        contact.setValue("3216931596");
        StepVerifier.create(contactRepositoryImplement.updateContact(contact))
                .consumeNextWith(response -> assertEquals("3216931596", response.getValue()))
                .verifyComplete();
    }

    @Test
    void saveContact() {
        contactRepositoryImplement.saveContact(contact)
                .subscribe(contactSaved -> StepVerifier
                        .create(contactRepositoryImplement.findContactsByClientSegmentAndMedium(contactSaved))
                        .expectNextCount(1)
                        .verifyComplete());
    }

    @Test
    void deleteContact() {
        contact.setContactWay("SMS");
        contact.setSegment("ALM");
        contactRepositoryImplement.findContactsByClientSegmentAndMedium(contact)
                .subscribe(contact -> StepVerifier
                        .create(contactRepositoryImplement.deleteContact(contact))
                        .verifyComplete());
    }
}