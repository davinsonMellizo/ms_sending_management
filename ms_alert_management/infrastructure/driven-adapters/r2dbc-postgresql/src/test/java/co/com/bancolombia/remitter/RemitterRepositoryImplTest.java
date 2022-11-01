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

    @Test
    void findAllRemitter() {
        StepVerifier.create(repositoryImpl.findAll())
                .consumeNextWith(allRemitters -> assertEquals(3, allRemitters.size()))
                .verifyComplete();
    }

    @Test
    void updateRemitter() {
        StepVerifier.create(repositoryImpl.updateRemitter(remitter))
                .consumeNextWith(status -> assertEquals(remitter.getId(), status.getActual().getId()))
                .verifyComplete();
    }

    @Test
    void saveRemitter() {
        remitter.setId(2);
        repositoryImpl.saveRemitter(remitter)
                .subscribe(alertSaved -> StepVerifier
                        .create(repositoryImpl.findRemitterById(alertSaved.getId()))
                        .expectNextCount(1)
                        .verifyComplete());
    }

    @Test
    void deleteRemitter() {
        remitter.setId(2);
        StepVerifier.create(repositoryImpl.deleteRemitterById(remitter.getId()))
                .consumeNextWith(s -> assertEquals(remitter.getId(), s))
                .verifyComplete();
    }
}