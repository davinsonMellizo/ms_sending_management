package co.com.bancolombia.sqs.listener.helper;

import co.com.bancolombia.sqs.listener.SQSProcessor;
import co.com.bancolombia.sqs.listener.config.SQSProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class SQSListenerTest {

    @InjectMocks
    private SQSListener sqsListener;

    @Mock
    private SQSProperties sqsProperties;

    @InjectMocks
    private SQSProcessor sqsProcessor;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);

        when(sqsProperties.getQueueUrl()).thenReturn("http://localhost:4566/00000000000/sqs-queue");
        when(sqsProperties.getNumberOfThreads()).thenReturn(1);
        when(sqsProperties.getEndpoint()).thenReturn("http://localhost:4566");
        when(sqsProperties.getWaitTimeSeconds()).thenReturn(1);
        when(sqsProperties.getMaxNumberOfMessages()).thenReturn(1);

        ReflectionTestUtils.setField(sqsListener, "processor", sqsProcessor);
    }

    @Test
    void handleMessage() {
        assertThat(sqsListener.start()).isNotNull();
    }
}
