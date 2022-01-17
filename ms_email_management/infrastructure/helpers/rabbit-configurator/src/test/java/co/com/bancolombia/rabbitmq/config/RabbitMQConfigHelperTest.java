package co.com.bancolombia.rabbitmq.config;

import co.com.bancolombia.rabbitmq.config.model.RabbitMQConnectionProperties;
import co.com.bancolombia.secretsmanager.api.GenericManager;
import co.com.bancolombia.secretsmanager.api.exceptions.SecretException;
import com.rabbitmq.client.ConnectionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;


public class RabbitMQConfigHelperTest {

    private static final String secretName = "secret-example";
    private static final String virtualHost = "/";
    private static final String hostname = "example.com";
    private static final String username = "user2";
    private static final String password = "pass2";
    private static final boolean SSL = true;
    private static final Integer port = 5432;
    private static final String METHOD_CONFIG_SSL = "configureSsl";

    private final RabbitMQConfigHelper helper = new RabbitMQConfigHelper(secretName);
    private final RabbitMQConnectionProperties properties = new RabbitMQConnectionProperties();

    @Mock
    private GenericManager genericManager;
    @Mock
    private ConnectionFactory factory;

    @BeforeEach
    public void init() throws SecretException {
        MockitoAnnotations.openMocks(this);
        properties.setVirtualhost(virtualHost);
        properties.setHostname(hostname);
        properties.setUsername(username);
        properties.setPassword(password);
        properties.setSsl(SSL);
        properties.setPort(port);

        when(genericManager.getSecret(secretName, RabbitMQConnectionProperties.class)).thenReturn(properties);
    }

    @Test
    public void getConnectionConfig() throws SecretException {
        assertThat(helper.getConnectionFactoryProvider(genericManager)).isNotNull();
    }

    @Test
    public void configureSslWithException() throws KeyManagementException, NoSuchAlgorithmException {
        doThrow(new NoSuchAlgorithmException()).when(factory).useSslProtocol();
        ReflectionTestUtils.invokeMethod(helper, METHOD_CONFIG_SSL, factory);
        assertThat(factory).isNotNull();
    }
}