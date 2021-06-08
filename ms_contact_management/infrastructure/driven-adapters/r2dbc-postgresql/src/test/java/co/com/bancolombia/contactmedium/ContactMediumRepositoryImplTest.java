package co.com.bancolombia.contactmedium;


import co.com.bancolombia.contact.ContactRepositoryImplement;
import co.com.bancolombia.enrollmentcontact.EnrollmentContactRepositoryImplement;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ContactMediumRepositoryImplTest {

    @Autowired
    private ContactMediumRepositoryImplement repositoryImplement;

    @Test
    public void findContactMediumByCode() {
        StepVerifier.create(repositoryImplement.findContactMediumByCode("SMS"))
                .expectNextCount(1)
                .verifyComplete();
    }
}