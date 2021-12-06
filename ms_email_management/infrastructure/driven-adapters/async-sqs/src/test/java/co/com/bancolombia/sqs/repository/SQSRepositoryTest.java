package co.com.bancolombia.sqs.repository;

import co.com.bancolombia.sqs.config.SQSProperties;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.model.InvalidMessageContentsException;
import com.amazonaws.services.sqs.model.SendMessageResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class SQSRepositoryTest {
    @InjectMocks
    private SQSRepository sqsRepository;
    @Mock
    private AmazonSQSAsync client;
    @Mock
    private SQSProperties properties;

    @BeforeEach
    public void init(){
        MockitoAnnotations.initMocks(this);
        when(properties.getUrl()).thenReturn("localhost");
    }
    @Test
    public void sendMessageTest(){
        when(client.sendMessage(any())).thenReturn(new SendMessageResult());
        StepVerifier.create(sqsRepository.putQueue("message"))
                .verifyComplete();
    }

    @Test
    public void sendMessageErrorTest(){
        when(client.sendMessage(any())).thenThrow(new InvalidMessageContentsException("error"));
        StepVerifier.create(sqsRepository.putQueue("message"))
                .expectError()
                .verify();
    }
}
