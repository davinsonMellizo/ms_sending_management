package co.com.bancolombia.sqs.repository;

import co.com.bancolombia.sqs.config.SQSProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.test.StepVerifier;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class SQSRepositoryTest {

    @InjectMocks
    private SQSRepository sqsRepository;
    @Mock
    private SqsAsyncClient client;
    @Mock
    private SQSProperties properties;

    @BeforeEach
    public void init(){
        MockitoAnnotations.openMocks(this);
        when(properties.getUrl()).thenReturn("localhost");
    }
    @Test
    void sendMessageTest(){
        when(client.sendMessage((SendMessageRequest) any()))
                .thenReturn(new CompletableFuture<>());
        StepVerifier.create(sqsRepository.putQueue("message"))
                .verifyComplete();
    }
}
