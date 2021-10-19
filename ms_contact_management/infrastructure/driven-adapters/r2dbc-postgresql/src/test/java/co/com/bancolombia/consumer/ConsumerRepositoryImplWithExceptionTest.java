package co.com.bancolombia.consumer;


import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.consumer.data.ConsumerMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ConsumerRepositoryImplWithExceptionTest {

    @InjectMocks
    private ConsumerRepositoryImplement consumerRepositoryImplement;
    @Mock
    private ConsumerRepository repository;
    @Spy
    private ConsumerMapper mapper = Mappers.getMapper(ConsumerMapper.class);


    @Test
    public void findConsumerByNameWithException() {
        when(repository.findById(anyString()))
                .thenReturn(Mono.error(RuntimeException::new));
        consumerRepositoryImplement.findConsumerById("CC")
                .as(StepVerifier::create)
                .expectError(TechnicalException.class)
                .verify();
    }

}