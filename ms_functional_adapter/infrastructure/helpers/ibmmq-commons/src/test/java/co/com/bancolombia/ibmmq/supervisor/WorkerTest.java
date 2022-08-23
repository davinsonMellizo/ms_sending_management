package co.com.bancolombia.ibmmq.supervisor;

import co.com.bancolombia.commons.utils.JsonUtils;
import co.com.bancolombia.ibmmq.jms.JmsManagement;
import co.com.bancolombia.ibmmq.model.ConnectionData;
import co.com.bancolombia.model.datatest.DataTest;
import co.com.bancolombia.model.log.LoggerBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.JMSRuntimeException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class WorkerTest {
    @Mock
    private JmsManagement config;
    @Mock
    private MessageListener listener;
    @Mock
    private Connection connection;
    @Mock
    private Session session;
    @Mock
    private MessageConsumer messageConsumer;
    @Mock
    private LoggerBuilder log;

    private static final ConnectionData connectionData = JsonUtils.stringToType(DataTest.config, ConnectionData.class);

    @BeforeEach
    public void init() {
        when(config.getConnectionData()).thenReturn(connectionData);
    }

    @Test
    void shouldRunUntilSuccess() throws JMSException {
        Worker worker = new Worker(config, listener, log);
        worker.setConnectionName("connFactory1");
        worker.run();
        verify(config).resetConnection("connFactory1");
    }

    @Test
    void shouldRunTimes() throws JMSException {
        Mockito.doThrow(new JMSRuntimeException(""))
                .doNothing()
                .when(config)
                .resetConnection("connFactory1");

        Worker worker = new Worker(config, listener, log);
        worker.setConnectionName("connFactory1");
        worker.run();
        verify(config, times(2)).resetConnection("connFactory1");
    }
}