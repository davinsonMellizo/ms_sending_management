package co.com.bancolombia.rabbitmq.config;

import co.com.bancolombia.d2b.model.secret.SyncSecretVault;
import co.com.bancolombia.log.LoggerBuilder;
import co.com.bancolombia.rabbitmq.config.model.RabbitMQConnectionProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RabbitMQConfigHelperTest {
    @Mock
    private SyncSecretVault secretsManager;
    @Mock
    private LoggerBuilder loggerBuilder;
    @InjectMocks
    private RabbitMQConfigHelper rabbitMQConfigHelper;

    @Test
    void connectionRabbitWhenSecretExistTest() {
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
        properties.setSsl(true);
        return properties;
    }

}
