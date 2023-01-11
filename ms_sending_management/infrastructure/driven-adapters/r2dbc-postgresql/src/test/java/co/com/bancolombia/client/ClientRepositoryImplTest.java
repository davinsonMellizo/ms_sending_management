package co.com.bancolombia.client;

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
    private ClientRepositoryImplement clientRepositoryImplement;


    @Test
    void findIdClient() {

        StepVerifier.create(clientRepositoryImplement.findClientByIdentification(1061772353L,0))
                .expectNextCount(1)
                .verifyComplete();
    }
}