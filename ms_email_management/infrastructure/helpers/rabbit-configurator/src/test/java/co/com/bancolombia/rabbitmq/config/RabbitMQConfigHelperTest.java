package co.com.bancolombia.rabbitmq.config;

import co.com.bancolombia.rabbitmq.config.model.RabbitMQConnectionProperties;
import co.com.bancolombia.secretsmanager.SecretsManager;
import co.com.bancolombia.secretsmanager.SecretsNameStandard;
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


public class RabbitMQConfigHelperTest {
    public static final String SECRET = "any-secret-dev";


    @Mock
    private SecretsManager secretsManager;
    @Mock
    private SecretsNameStandard secretsNameStandard;

    @InjectMocks
    private RabbitMQConfigHelper rabbitMQConfigHelper;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void connectionRabbitWhenSecretExistTest(){
        when(secretsManager.getSecret(anyString(), any())).thenReturn(Mono.just(properties()));
        when(secretsNameStandard.secretForRabbitMQ()).thenReturn(Mono.just("name"));
        assertThat(rabbitMQConfigHelper.getConnectionFactoryProvider()).isNotNull();
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
