package co.com.bancolombia.contactmedium;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ContactMediumRepositoryImplTest {

    @Autowired
    private ContactMediumRepositoryImplement repositoryImplement;

    @Test
    void findContactMediumByCode() {
        StepVerifier.create(repositoryImplement.findContactMediumByCode("SMS"))
                .expectNextCount(1)
                .verifyComplete();
    }
}