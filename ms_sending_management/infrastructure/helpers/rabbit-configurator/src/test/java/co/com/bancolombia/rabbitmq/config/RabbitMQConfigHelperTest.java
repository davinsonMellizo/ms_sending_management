package co.com.bancolombia.rabbitmq.config;

import co.com.bancolombia.d2b.model.secret.SyncSecretVault;
import co.com.bancolombia.rabbitmq.config.model.RabbitMQConnectionProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class RabbitMQConfigHelperTest {
    public static final String SECRET = "any-secret-dev";

    private SyncSecretVault secretsManager = mock(SyncSecretVault.class);

    private RabbitMQConfigHelper rabbitMQConfigHelper = new RabbitMQConfigHelper(null, secretsManager);

    @BeforeEach
    public void init() throws NoSuchFieldException, IllegalAccessException {
        MockitoAnnotations.initMocks(this);
        final Field secretName = RabbitMQConfigHelper.class.getDeclaredField("secretName");
        secretName.setAccessible(true);
        secretName.set(rabbitMQConfigHelper, SECRET);
    }

    @Test
    void connectionRabbitWhenSecretExistTest() {
        when(secretsManager.getSecret(anyString(), any())).thenReturn(properties());
        assertThat(rabbitMQConfigHelper.getConnectionFactoryProvider()).isNotNull();
    }

    private RabbitMQConnectionProperties properties() {
        RabbitMQConnectionProperties properties = new RabbitMQConnectionProperties();
        properties.setHostname("any-host");
        properties.setUsername("this-is-for-test");
        properties.setPassword("this-is-for-test");
        properties.setVirtualhost("/");
        properties.setPort(8080);
        properties.setSsl(true);
        return properties;
    }
}
