package co.com.bancolombia.rabbitmq.config;

import co.com.bancolombia.d2b.model.secret.SyncSecretVault;
import co.com.bancolombia.rabbitmq.config.model.RabbitMQConnectionProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class RabbitMQConfigHelperTest {
    @Mock
    private SyncSecretVault secretsManager;

    @InjectMocks
    private RabbitMQConfigHelper rabbitMQConfigHelper;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void connectionRabbitWhenSecretExistTest() {
        when(secretsManager.getSecret(any(), any())).thenReturn(properties());
        assertThat(rabbitMQConfigHelper.getConnectionFactoryProvider()).isNotNull();
    }

    private RabbitMQConnectionProperties properties() {
        RabbitMQConnectionProperties properties = new RabbitMQConnectionProperties();
        properties.setHostname("any-host");
        properties.setUsername("this-is-for-test");
        properties.setPassword("this-is-for-test");
        properties.setVirtualhost("/");
        properties.setPort(8080);
        return properties;
    }
}
