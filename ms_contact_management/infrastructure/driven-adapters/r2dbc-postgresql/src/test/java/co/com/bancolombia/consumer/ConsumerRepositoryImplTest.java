package co.com.bancolombia.consumer;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ConsumerRepositoryImplTest {

    @Autowired
    private ConsumerRepositoryImplement consumerRepositoryImplement;

    @Test
    void findStateByName() {
        StepVerifier.create(consumerRepositoryImplement.findConsumerById("ALM"))
                .expectNextCount(1)
                .verifyComplete();
    }
}