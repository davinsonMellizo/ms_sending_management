package co.com.bancolombia.ibmmq;

import co.com.bancolombia.ibmmq.jms.JmsManagement;
import co.com.bancolombia.ibmmq.model.ConnectionData;
import co.com.bancolombia.ibmmq.model.QueueDto;
import co.com.bancolombia.model.datatest.DataTest;
import co.com.bancolombia.model.log.LoggerBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MqConnectorTest {

    @InjectMocks
    private MqConnector mqConnector;
    @Mock
    private JmsManagement connectionsManagement;
    @Mock
    private ConnectionData connectionData;
    @Mock
    private JMSContext jmsContext;
    @Mock
    private TextMessage txtMessage;
    @Mock
    private JMSProducer msgP;
    @Mock
    private Destination destination;
    @Mock
    private MessageListener msgListener;
    @Mock
    private Connection connection;
    @Mock
    private Session session;
    @Mock
    private MessageConsumer messageConsumer;
    @Mock
    private LoggerBuilder loggerBuilder;

    private static final ObjectMapper mapper = new ObjectMapper();
    private QueueDto queue;

    @BeforeEach
    public void init() throws IOException {
        queue = mapper.readValue(DataTest.QueueDto, QueueDto.class);
    }

    @Test
    public void sendMessageToQueueTest() throws JMSException {
        when(connectionsManagement.getConnectionData()).thenReturn(connectionData);
        when(connectionData.getQueueFromTransaction(anyString())).thenReturn(queue);
        when(connectionData.getQueueResponseFromTransaction(anyString())).thenReturn(queue);
        when(connectionsManagement.getTextMessage(any(), any())).thenReturn(txtMessage);
        when(connectionsManagement.getJmsProducer(any())).thenReturn(msgP);
        when(connectionsManagement.getQueue(any())).thenReturn(destination);
        when(txtMessage.getJMSMessageID()).thenReturn("123456");
        StepVerifier.create(mqConnector.sendMessageToQueue("test", "test", "123456"))
                .expectNext("123456")
                .verifyComplete();
    }

    @Test
    public void givenErrorWhenSendMessageToQueueTest() throws JMSException {
        when(connectionsManagement.getConnectionData()).thenReturn(connectionData);
        when(connectionData.getQueueFromTransaction(anyString())).thenReturn(queue);
        when(connectionData.getQueueResponseFromTransaction(anyString())).thenReturn(queue);
        when(connectionsManagement.getTextMessage(any(), any())).thenReturn(txtMessage);
        StepVerifier.create(mqConnector.sendMessageToQueue("test", "test", "123456")).expectError().verify();
    }

    @Test
    public void eventConsumerTest(){
        mqConnector.eventConsumer(queue, msgListener);
    }
}