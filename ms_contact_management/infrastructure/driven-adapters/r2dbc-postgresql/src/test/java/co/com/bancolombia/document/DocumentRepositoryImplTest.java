package co.com.bancolombia.document;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class DocumentRepositoryImplTest {

    @Autowired
    private DocumentRepositoryImplement documentRepositoryImplement;

    @Test
    void findStateByName() {
        StepVerifier.create(documentRepositoryImplement.getDocument("CC"))
                .expectNextCount(1)
                .verifyComplete();
    }
}