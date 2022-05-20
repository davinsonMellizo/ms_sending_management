package co.com.bancolombia.newness;

import co.com.bancolombia.model.newness.Newness;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class NewnessRepositoryImplTest {

    @Autowired
    private NewnessRepositoryImplement repositoryImplement;

    @Test
    public void saveNewness() {
        Newness newness = new Newness();
        newness.setDocumentNumber(11111L);
        newness.setDocumentType(1);
        newness.setDateCreation(LocalDateTime.now());
        newness.setDateFirstInscription(LocalDateTime.now());
        StepVerifier.create(repositoryImplement.saveNewness(newness))
                .expectNextCount(1)
                .verifyComplete();
    }
}