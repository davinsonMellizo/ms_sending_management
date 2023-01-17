package co.com.bancolombia.sqs.listener;

import co.com.bancolombia.usecase.sendalert.SendAlertUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import software.amazon.awssdk.services.sqs.model.Message;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class SQSProcessorTest {
    @InjectMocks
    SQSProcessor sqsProcessor;

    @Mock
    private SendAlertUseCase useCase;

    @Mock
    private ObjectMapper objectMapper;

    private Message message;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        String msgSQS = "{\"Records\":[{\"s3\": {\"bucket\":{\"name\":\"massive-bucket\",\"arn\":\"arn:aws:s3:::massive-bucket\"},\"object\":{\"key\":\"macd/ChannelType%3DEMAIL/formato_masivos.csv\"}}}]}";
        message = Message.builder().messageId("1").body(msgSQS).build();
    }

    @Test
    void applyTest() {
        when(useCase.getTemplate(any()))
                .thenReturn(Mono.empty());

        StepVerifier.create(sqsProcessor.apply(message))
                .expectSubscription()
                .thenConsumeWhile(x -> true)
                .verifyComplete();
    }

}
