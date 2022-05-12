package co.com.bancolombia.priority;

import co.com.bancolombia.model.priority.Priority;
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
public class PriorityRepositoryImplTest {

    @Autowired
    private PriorityRepositoryImplement repositoryImpl;
    private final Priority priority = new Priority();

    @BeforeEach
    public void init() {
        priority.setId(1);
        priority.setCode(1);
        priority.setDescription("description");
        priority.setIdProvider("HJK");
    }

    @Test
    void findIdPriority() {
        StepVerifier.create(repositoryImpl.findPriorityById(priority.getId()))
                .consumeNextWith(priorityFound -> assertEquals(priority.getId(), priorityFound.getId()))
                .verifyComplete();
    }

    @Test
    void findAllPriorities() {
        StepVerifier.create(repositoryImpl.findAllByProvider(priority.getIdProvider()))
                .consumeNextWith(allPrior -> assertEquals(3, allPrior.size()))
                .verifyComplete();
    }

    @Test
    void updatePriority() {
        StepVerifier.create(repositoryImpl.updatePriority(priority))
                .consumeNextWith(status -> assertEquals(priority.getId(), status.getActual().getId()))
                .verifyComplete();
    }

    @Test
    void saveProvider() {
        priority.setId(3);
        repositoryImpl.savePriority(priority)
                .subscribe(pr -> StepVerifier
                        .create(repositoryImpl.findPriorityById(pr.getId()))
                        .expectNextCount(1)
                        .verifyComplete());
    }

    @Test
    void deleteProvider() {
        priority.setId(3);
        StepVerifier.create(repositoryImpl.deletePriorityById(priority.getId()))
                .consumeNextWith(s -> assertEquals(priority.getId(), s))
                .verifyComplete();
    }
}