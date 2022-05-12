package co.com.bancolombia.consumer;

import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.consumer.data.ConsumerData;
import co.com.bancolombia.consumer.data.ConsumerMapper;
import co.com.bancolombia.model.consumer.Consumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class ConsumerRepositoryImplExceptionTest {

    @InjectMocks
    private ConsumerRepositoryImplement repositoryImpl;
    @Mock
    private ConsumerRepository repository;
    @Spy
    private ConsumerMapper mapper = Mappers.getMapper(ConsumerMapper.class);

    private final Consumer consumer = new Consumer();
    private final ConsumerData consumerData = new ConsumerData();

    @BeforeEach
    public void init() {
        consumer.setId("1");
        consumer.setSegment("123asv");

        consumerData.setId("1");
        consumerData.setSegment("123asv");
    }


    @Test
    void findConsumerByIdWithException() {
        when(repository.findById(anyString()))
                .thenReturn(Mono.error(RuntimeException::new));
        repositoryImpl.findConsumerById(consumer.getId())
                .as(StepVerifier::create)
                .expectError(TechnicalException.class)
                .verify();
    }

}