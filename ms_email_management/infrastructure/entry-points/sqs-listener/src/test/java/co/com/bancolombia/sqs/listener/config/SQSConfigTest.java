package co.com.bancolombia.sqs.listener.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class SQSConfigTest {

    @InjectMocks
    private SQSConfig sqsConfig;

    @Mock
    private SqsAsyncClient sqsAsyncClient;

    @Mock
    private SQSProperties sqsProperties;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        when(sqsProperties.getEndpoint()).thenReturn("http://localhost:4566");
        when(sqsProperties.getQueueUrl()).thenReturn("http://localhost:4566/00000000000/massive-sqs");
        when(sqsProperties.getWaitTimeSeconds()).thenReturn(20);
        when(sqsProperties.getMaxNumberOfMessages()).thenReturn(10);
        when(sqsProperties.getNumberOfThreads()).thenReturn(1);
    }

    @Test
    void sqsListenerIsNotNull() {
        assertThat(sqsConfig.sqsListener(sqsAsyncClient, sqsProperties, message -> Mono.empty())).isNotNull();
    }

    @Test
    void configSqsIsNotNull() {
        assertThat(sqsConfig.configSqs(sqsProperties)).isNotNull();
    }
}
