package co.com.bancolombia.ibmmq.jms;

import co.com.bancolombia.commons.config.AwsProperties;
import co.com.bancolombia.ibmmq.model.QueueDto;
import co.com.bancolombia.model.log.LoggerBuilder;
import co.com.bancolombia.s3bucket.S3AsynOperations;
import com.ibm.mq.jms.MQQueue;
import com.ibm.mq.jms.MQTemporaryQueue;
import com.ibm.msg.client.jms.JmsConnectionFactory;
import com.ibm.msg.client.jms.JmsTemporaryQueue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Mono;

import javax.jms.TextMessage;
import javax.jms.JMSProducer;
import javax.jms.JMSException;
import javax.jms.Connection;
import javax.jms.JMSContext;
import javax.jms.JMSConsumer;
import javax.jms.ExceptionListener;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.Queue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class JmsManagementTest {

    @Spy
    @InjectMocks
    private JmsManagement management;
    @Mock
    private S3AsynOperations s3AsynOperations;
    @Mock
    private JmsExtConnectionFactory connectionFactory;
    @Mock
    private JmsConnectionFactory jmsConnectionFactory;
    @Mock
    private LoggerBuilder loggerBuilder;
    @Spy
    private AwsProperties awsProperties = buildAwsProperties();
    @Mock
    private ExceptionListener exceptionListener = new ExceptionListener() {
        @Override
        public void onException(JMSException exception) {
        }
    };
    @Mock
    private ProducerManagement producerManagement;
    @Mock
    private JMSContext jmsContext;
    @Mock
    private Connection connection;
    @Mock
    private TextMessage textMessage;
    @Mock
    private JMSProducer jmsProducer;
    @Mock
    private JMSConsumer jmsConsumer;
    @Mock
    private MessageConsumer messageConsumer;
    @Mock
    private Session session;
    @Mock
    private Queue destination;
    @Mock
    private JmsTemporaryQueue jmsTemporaryQueue;
    @Mock
    private MQQueue moQueue;
    @Mock
    private MQTemporaryQueue temporaryQueue;

    @BeforeEach
    public void init() {
        ReflectionTestUtils.setField(management, "exceptionListener", exceptionListener);
        when(s3AsynOperations.getFileAsInputStream(any(), any()))
                .thenReturn(Mono.just(getInputStream("s3_config-listener-mq.json")));
        when(s3AsynOperations.getFileAsString(any(), any()))
                .thenReturn(Mono.just(loadFileConfig("s3_config-listener-mq.json")));
        when(connectionFactory.connectionFactory(any())).thenReturn(jmsConnectionFactory);
        try {
            doReturn(true).when(management).saveToFile(any(), any());
            when(jmsConnectionFactory.createContext()).thenReturn(jmsContext);
            when(jmsContext.createQueue(any())).thenReturn(moQueue);
            when(jmsConnectionFactory.createConnection()).thenReturn(connection);
            when(connection.createSession()).thenReturn(session);
            when(session.createTemporaryQueue()).thenReturn(temporaryQueue);
            when(temporaryQueue.getQueueName()).thenReturn("temporaryQueue");
            management.init();
        } catch (IOException | JMSException e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    void getTextMessage() {
        when(jmsContext.createTextMessage(any())).thenReturn(textMessage);
        assertThat(management.getTextMessage("connFactory1", "message")).isInstanceOf(TextMessage.class);
    }

    @Test
    void getJmsProducer() {
        when(jmsContext.createProducer()).thenReturn(jmsProducer);
        assertThat(management.getJmsProducer("connFactory1")).isNotNull();
        verify(jmsContext).createProducer();
    }

    @Test
    void getQueueConsumer() {
        QueueDto dto = new QueueDto();
        dto.setConnection("connFactory1");
        dto.setName("DEV.QUEUE.1");
        assertThat(management.getQueue(dto)).isNotNull();
    }

    @Test
    void getQueueConsumerTemporary() {
        QueueDto dto = new QueueDto();
        dto.setConnection("connFactory1");
        dto.setName("DEV.QUEUE.1");
        dto.setTemporary(true);
        assertThat(management.getQueue(dto)).isNotNull();
    }

    @Test
    void getQueueCProducer() {
        QueueDto dto = new QueueDto();
        dto.setConnection("connFactory1");
        dto.setName("DEV.QUEUE.1");
        assertThat(management.getQueue(dto)).isNotNull();
    }

    @Test
    void resetConnection() {
        management.resetConnection("connFactory1");
        verify(connectionFactory, atLeast(2)).connectionFactory(any());
    }

    @Test
    void setMessageListener() {
        try {
            QueueDto dto = new QueueDto();
            dto.setConnection("connFactory1");
            dto.setName("DEV.QUEUE.1");
            when(connection.createSession()).thenReturn(session);
            when(session.createConsumer(any())).thenReturn(messageConsumer);

            management.setMessageListener(dto, msg -> {
            });
            verify(connection, times(2)).createSession();
            verify(session).createConsumer(any());
            verify(messageConsumer).setMessageListener(any());
        } catch (JMSException e) {
            Assertions.fail(e.getMessage());
        }
    }

    private AwsProperties buildAwsProperties() {
        AwsProperties properties = new AwsProperties();
        properties.setRegion("us-east");
        properties.setS3(new AwsProperties.S3());
        properties.getS3().setEndpoint("endpoint");
        properties.getS3().setBucket("bucket");
        properties.getS3().setConfigListenerMqKey("configListenerMqKey");
        return properties;
    }

    private String loadFileConfig(String filename) {
        try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename)) {
            return new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));
        } catch (IOException e) {
            Assertions.fail(e.getMessage());
            return null;
        }
    }

    private InputStream getInputStream(String filename) {
        try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename)) {
            return inputStream;
        } catch (IOException e) {
            Assertions.fail(e.getMessage());
            return null;
        }
    }
}