package co.com.bancolombia.contact;

import co.com.bancolombia.model.contact.Contact;
import co.com.bancolombia.model.message.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ContactRepositoryImplTest {

    @Autowired
    private ContactRepositoryImplement contactRepositoryImplement;
    private final Contact contact = new Contact();

    @BeforeEach
    public void init() {


    }

    @Test
    void findIdContact() {
        Message message = new Message();
        message.setDocumentNumber(1061772353L);
        message.setDocumentType(0);
        message.setConsumer("ALM");
        StepVerifier.create(contactRepositoryImplement.findAllContactsByClient(message))
                .expectNextCount(2)
                .verifyComplete();
    }
}