package co.com.bancolombia.state;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class StateRepositoryImplTest {

    @Autowired
    private StateRepositoryImplement stateRepositoryImplement;

    @Test
    public void findStateByName() {
        StepVerifier.create(stateRepositoryImplement.findStateByName("Active"))
                .expectNextCount(1)
                .verifyComplete();
    }
}