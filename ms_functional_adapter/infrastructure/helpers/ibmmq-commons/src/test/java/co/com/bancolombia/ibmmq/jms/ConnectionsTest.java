package co.com.bancolombia.ibmmq.jms;

import com.ibm.msg.client.jms.JmsConnectionFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.jms.Connection;
import javax.jms.ExceptionListener;
import javax.jms.JMSContext;
import javax.jms.JMSException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class ConnectionsTest {

    @Mock
    private JmsConnectionFactory factory;
    @Mock
    private ExceptionListener exceptionListener;
    @Mock
    private JMSContext context;
    @Mock
    private Connection connection;

    @Test
    public void validCreateObject() throws JMSException {
        when(factory.createContext()).thenReturn(context);
        when(factory.createConnection()).thenReturn(connection);
        ConnectionJms conn = new ConnectionJms(factory,exceptionListener);
        assertThat(conn).isNotNull();
    }
}