package co.com.bancolombia.rabbitmq.config;

import co.com.bancolombia.d2b.model.secret.SyncSecretVault;
import co.com.bancolombia.rabbitmq.config.dual.sender.RabbitMQDualConfigHelper;
import co.com.bancolombia.rabbitmq.config.model.RabbitMQConnectionProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


class RabbitMQConfigHelperTest {
    public static final String SECRET = "any-secret-dev";


    @Mock
    private SyncSecretVault secretsManager;

    @InjectMocks
    private RabbitMQDualConfigHelper rabbitMQConfigHelper;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void connectionRabbitWhenSecretExistTest(){
        when(secretsManager.getSecret(anyString(), any())).thenReturn(Mono.just(properties()));
        assertThat(rabbitMQConfigHelper.connectionFactoryProvider()).isNotNull();
    }

    private RabbitMQConnectionProperties properties(){
        RabbitMQConnectionProperties properties = new RabbitMQConnectionProperties();
        properties.setHostname("any-host");
        properties.setUsername("this-is-for-test");
        properties.setPassword("this-is-for-test");
        properties.setVirtualhost("/");
        properties.setPort(8080);
        return properties;
    }
}
