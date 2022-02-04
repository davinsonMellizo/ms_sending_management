package co.com.bancolombia.ibmmq.config;

import co.com.bancolombia.ibmmq.jms.JmsExtConnectionFactory;
import co.com.bancolombia.ibmmq.jms.JmsManagement;
import co.com.bancolombia.ibmmq.model.ConnectionDTO;
import co.com.bancolombia.ibmmq.model.ConnectionData;
import com.ibm.msg.client.jms.JmsConnectionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;

import javax.jms.Connection;
import javax.jms.ConnectionMetaData;
import javax.jms.JMSException;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ConnectionsManagementHealthIndicatorTest {
    @Mock
    private JmsManagement cm;
    @Mock
    private JmsExtConnectionFactory extFactory;
    @Mock
    private JmsConnectionFactory factory;
    @Mock
    private Connection connection;
    @Mock
    private ConnectionMetaData connectionMetaData;
    @Mock
    private ConnectionData connectionData;

    private static final String connName = "connFactory1";
    private static final String qm = "QM1";
    public static final String QUEUE_MANAGER = "queueManager";

    @BeforeEach
    public void init() throws JMSException {
        when(cm.getConnectionData()).thenReturn(connectionData);
        when(connectionData.getConnections()).thenReturn(List.of(new ConnectionDTO()));
        when(cm.getConnectionFactory()).thenReturn(extFactory);
        when(extFactory.connectionFactory(any())).thenReturn(factory);
        when(factory.createConnection()).thenReturn(connection);

    }

    @Test
    public void shouldBeUp() throws JMSException {
        when(connection.getMetaData()).thenReturn(connectionMetaData);
        when(connectionMetaData.getJMSProviderName()).thenReturn("jMSProviderName");
        // Arrange
        doNothing().when(connection).start();
        doNothing().when(connection).close();
        ConnectionsManagementHealthIndicator indicator = new ConnectionsManagementHealthIndicator(cm);
        // Act
        Health health = indicator.getHealth(true);
        // Assert
        assertThat(health.getStatus()).isEqualTo(Status.UP);
        assertThat(((Map)health.getDetails().entrySet().stream().findFirst().get().getValue()).get("provider"))
                .isEqualTo("jMSProviderName");
    }

    @Test
    public void shouldBeDown() throws JMSException {
        // Arrange
        doThrow(new JMSException("Invalid Status")).when(connection).start();
        ConnectionsManagementHealthIndicator indicator = new ConnectionsManagementHealthIndicator(cm);
        // Act
        Health health = indicator.getHealth(true);
        // Assert
        assertThat(health.getStatus()).isEqualTo(Status.DOWN);
        assertThat(((Map)health.getDetails().entrySet().stream().findFirst().get().getValue()).get("error"))
                .isEqualTo("javax.jms.JMSException: Invalid Status");
    }
}