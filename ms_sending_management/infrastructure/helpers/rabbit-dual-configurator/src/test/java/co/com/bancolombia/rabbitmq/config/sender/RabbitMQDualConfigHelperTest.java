package co.com.bancolombia.rabbitmq.config.sender;

import co.com.bancolombia.d2b.model.secret.SyncSecretVault;
import co.com.bancolombia.model.log.LoggerBuilder;
import co.com.bancolombia.rabbitmq.config.dual.receiver.CommandListenersDualConfig;
import co.com.bancolombia.rabbitmq.config.dual.sender.RabbitDirectAsyncGateway;
import co.com.bancolombia.rabbitmq.config.dual.sender.RabbitMQDualConfigHelper;
import co.com.bancolombia.rabbitmq.config.model.RabbitMQConnectionProperties;
import com.rabbitmq.client.ConnectionFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.async.impl.DiscardNotifier;
import org.reactivecommons.async.impl.HandlerResolver;
import org.reactivecommons.async.impl.config.BrokerConfig;
import org.reactivecommons.async.impl.config.RabbitProperties;
import org.reactivecommons.async.impl.config.props.AsyncProps;
import org.reactivecommons.async.impl.config.props.BrokerConfigProps;
import org.reactivecommons.async.impl.converters.MessageConverter;
import org.reactivecommons.async.impl.ext.CustomErrorReporter;
import org.reactivecommons.async.impl.listeners.ApplicationCommandListener;
import org.reactivecommons.async.impl.reply.ReactiveReplyRouter;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;

import java.lang.reflect.Field;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RabbitMQDualConfigHelperTest {
    @MockBean
    private LoggerBuilder logger;

    private final RabbitProperties properties = new RabbitProperties();
    private final BrokerConfigProps props = mock(BrokerConfigProps.class);
    private final SyncSecretVault secretsManager =  mock(SyncSecretVault.class);
    private RabbitMQDualConfigHelper config = new RabbitMQDualConfigHelper(logger, props, secretsManager);

    private final BrokerConfig brokerConfig = mock(BrokerConfig.class);
    private final ReactiveReplyRouter router = mock(ReactiveReplyRouter.class);
    private final ConnectionFactory factory = mock(ConnectionFactory.class);
    private final MessageConverter messageConverter = mock(MessageConverter.class);

    @BeforeEach
    public void init() throws NoSuchFieldException, IllegalAccessException {
        final Field appName = RabbitMQDualConfigHelper.class.getDeclaredField("appName");
        appName.setAccessible(true);
        appName.set(config, "queue");

        final Field secretName = RabbitMQDualConfigHelper.class.getDeclaredField("secretName");
        secretName.setAccessible(true);
        secretName.set(config, "secretName");
    }

    @Test
    void rabbitDirectAsyncGatewayDual() {
        when(secretsManager.getSecret(anyString(), any())).thenReturn(properties());
        final RabbitDirectAsyncGateway rabbitDirectAsyncGateway = config.rabbitDirectAsyncGatewayDual(
                brokerConfig,
                router,
                props,
                properties,
                factory,
                messageConverter
        );
        Assertions.assertThat(rabbitDirectAsyncGateway).isNotNull();
    }

    @Test
    void connectionFactoryProvider() {
        when(secretsManager.getSecret(anyString(), any())).thenReturn(properties());
        final ConnectionFactory factory = config.connectionFactoryProvider();
        Assertions.assertThat(factory).isNotNull();
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
