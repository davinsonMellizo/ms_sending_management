package co.com.bancolombia.consumer;

import co.com.bancolombia.config.model.consumer.Consumer;
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
public class ConsumerRepositoryImplTest {

    @Autowired
    private ConsumerRepositoryImplement repositoryImpl;
    private final Consumer consumer = new Consumer();

    @BeforeEach
    public void init() {
        consumer.setId("1");
        consumer.setCode("4567aaa");
    }

    @Test
    public void findIdConsumer() {
        StepVerifier.create(repositoryImpl.findConsumerById(consumer.getId()))
                .consumeNextWith(consumerFound -> assertEquals(consumer.getId(), consumerFound.getId()))
                .verifyComplete();
    }

    @Test
    public void findAllConsumer() {
        StepVerifier.create(repositoryImpl.findAll())
                .consumeNextWith(consumers -> assertEquals(3, consumers.size()))
                .verifyComplete();
    }

    @Test
    public void updateConsumer() {
        StepVerifier.create(repositoryImpl.updateConsumer(consumer))
                .consumeNextWith(status -> assertEquals(consumer.getId(), status.getActual().getId()))
                .verifyComplete();
    }

    @Test
    public void saveConsumer() {
        consumer.setId("2");
        repositoryImpl.saveConsumer(consumer)
                .subscribe(consumer -> StepVerifier
                        .create(repositoryImpl.findConsumerById(consumer.getId()))
                        .expectNextCount(1)
                        .verifyComplete());
    }

    @Test
    public void deleteConsumer() {
        consumer.setId("1");
        StepVerifier.create(repositoryImpl.deleteConsumerById(consumer.getId()))
                .consumeNextWith(s -> assertEquals(consumer.getId(), s))
                .verifyComplete();
    }
}