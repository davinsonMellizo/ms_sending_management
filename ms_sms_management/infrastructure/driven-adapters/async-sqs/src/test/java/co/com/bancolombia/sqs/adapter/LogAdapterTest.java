package co.com.bancolombia.sqs.adapter;

import co.com.bancolombia.model.log.Log;
import co.com.bancolombia.sqs.repository.SQSRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class LogAdapterTest {
    @InjectMocks
    private LogAdapter logAdapter;
    @Mock
    private SQSRepository sqsRepository;

    @BeforeEach
    public void init(){
        MockitoAnnotations.initMocks(this);
        when(sqsRepository.putQueue(any())).thenReturn(Mono.empty());
    }

    @Test
    public void sendLogTest(){
        StepVerifier.create(logAdapter.putLogToSQS(new Log()))
                .expectNextCount(1)
                .verifyComplete();
    }
}
