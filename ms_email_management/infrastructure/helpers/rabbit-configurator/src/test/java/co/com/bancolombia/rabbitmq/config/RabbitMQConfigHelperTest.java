package co.com.bancolombia.rabbitmq.config;

import co.com.bancolombia.d2b.model.secret.SyncSecretVault;
import co.com.bancolombia.rabbitmq.config.model.RabbitMQConnectionProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class RabbitMQConfigHelperTest {
    public static final String SECRET = "any-secret-dev";

    private SyncSecretVault secretsManager= mock(SyncSecretVault.class);

    private RabbitMQConfigHelper rabbitMQConfigHelper= new RabbitMQConfigHelper(null, secretsManager);


    @BeforeEach
    public void init() throws NoSuchFieldException, IllegalAccessException {
        MockitoAnnotations.openMocks(this);
        final Field secretName;
        secretName = RabbitMQConfigHelper.class.getDeclaredField("secretName");
        secretName.setAccessible(true);
        secretName.set(rabbitMQConfigHelper, SECRET);;
    }
    @Test
    void connectionRabbitWhenSecretExistTest() {
        when(secretsManager.getSecret(anyString(), any())).thenReturn(properties());
        assertThat(rabbitMQConfigHelper.getConnectionFactoryProvider()).isNotNull();
    }

    @Test
    void connectionRabbitWhenSecretExistSSLTest() {
        when(secretsManager.getSecret(anyString(), any())).thenReturn(propertiesSSL());
        assertThat(rabbitMQConfigHelper.getConnectionFactoryProvider()).isNotNull();
    }

    private RabbitMQConnectionProperties properties() {
        RabbitMQConnectionProperties properties = new RabbitMQConnectionProperties();
        properties.setSsl(false);
        properties.setHostname("any-host");
        properties.setUsername("this-is-for-test");
        properties.setPassword("this-is-for-test");
        properties.setVirtualhost("/");
        properties.setPort(8080);
        return properties;
    }

    private RabbitMQConnectionProperties propertiesSSL() {
        RabbitMQConnectionProperties properties = new RabbitMQConnectionProperties();
        properties.setSsl(true);
        properties.setHostname("any-host");
        properties.setUsername("this-is-for-test");
        properties.setPassword("this-is-for-test");
        properties.setVirtualhost("/");
        properties.setPort(8080);
        return properties;
    }
}
