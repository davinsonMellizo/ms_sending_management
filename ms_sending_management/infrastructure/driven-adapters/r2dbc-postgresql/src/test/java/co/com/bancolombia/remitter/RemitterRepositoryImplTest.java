package co.com.bancolombia.remitter;

import co.com.bancolombia.model.remitter.Remitter;
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
class RemitterRepositoryImplTest {

    @Autowired
    private RemitterRepositoryImplement repositoryImpl;
    private final Remitter remitter = new Remitter();

    @BeforeEach
    public void init() {
        remitter.setId(0);
        remitter.setMail("mail@gmail.com");
        remitter.setState("Activo");
    }

    @Test
    void findIdRemitter() {
        StepVerifier.create(repositoryImpl.findRemitterById(remitter.getId()))
                .consumeNextWith(remitterFound -> assertEquals(remitter.getId(), remitterFound.getId()))
                .verifyComplete();
    }

}