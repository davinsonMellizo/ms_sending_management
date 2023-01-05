package co.com.bancolombia.rabbitmq.config.dual;

import co.com.bancolombia.d2b.model.secret.SyncSecretVault;
import co.com.bancolombia.model.log.LoggerBuilder;
import co.com.bancolombia.rabbitmq.config.model.RabbitMQConnectionProperties;
import com.rabbitmq.client.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import org.reactivecommons.async.impl.communications.ReactiveMessageSender;
import org.reactivecommons.async.impl.config.BrokerConfig;
import org.reactivecommons.async.impl.config.ConnectionFactoryProvider;
import org.reactivecommons.async.impl.config.RabbitMqConfig;
import org.reactivecommons.async.impl.config.RabbitProperties;
import org.reactivecommons.async.impl.config.props.BrokerConfigProps;
import org.reactivecommons.async.impl.converters.MessageConverter;
import org.reactivecommons.async.impl.reply.ReactiveReplyRouter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.rabbitmq.RabbitFlux;
import reactor.rabbitmq.ReceiverOptions;
import reactor.rabbitmq.SenderOptions;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

@Configuration
@RequiredArgsConstructor
public class RabbitMQDualConfigHelper {
    private final LoggerBuilder logger;
    private final BrokerConfigProps props;
    private final SyncSecretVault secretsManager;
    private static final String TLS = "TLSv1.2";
    private static final String FAIL_MSG = "Error creating ConnectionFactoryProvider";

    @Value("${spring.application.name}")
    private String appName;
    @Value("${adapters.secrets-manager.secret-rabbit-dual}")
    private String secretName;
    private final RabbitMqConfig rabbitMqConfig;

    @Bean("rabbitDirectAsyncGatewayDual")
    public RabbitDirectAsyncGateway rabbitDirectAsyncGatewayDual(BrokerConfig config, ReactiveReplyRouter router,
                                                             BrokerConfigProps brokerConfigProps,
                                                             RabbitProperties rabbitProperties,
                                                             ConnectionFactory factory,
                                                             MessageConverter converter){
        final ConnectionFactoryProvider provider = () -> factory;
        SenderOptions senderOptions = rabbitMqConfig.reactiveCommonsSenderOptions(provider,rabbitProperties);
        ReactiveMessageSender sender = rabbitMqConfig.messageSender(converter, brokerConfigProps, senderOptions);

        return new RabbitDirectAsyncGateway(config, router, sender, props.getDirectMessagesExchangeName(), converter);
    }

    private RabbitMQConnectionProperties rabbitProperties() {
        return secretsManager.getSecret(secretName, RabbitMQConnectionProperties.class);
    }

    @Bean
    public ConnectionFactory connectionFactoryProvider(){
        RabbitMQConnectionProperties properties = rabbitProperties();
        final ConnectionFactory factory = new ConnectionFactory();
        PropertyMapper map = PropertyMapper.get();

        map.from(properties::getPort).to(factory::setPort);
        map.from(properties::getHostname).whenNonNull().to(factory::setHost);
        map.from(properties::getUsername).whenNonNull().to(factory::setUsername);
        map.from(properties::getPassword).whenNonNull().to(factory::setPassword);
        map.from(properties::isSsl).whenTrue().as(isSsl -> factory).to(this::configureSsl);

        return factory;
    }

    private void configureSsl(ConnectionFactory factory) {
        try {
            SSLContext c = SSLContext.getInstance(TLS);
            c.init(null, null, null);

            factory.useSslProtocol(c);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            logger.info(String.format(FAIL_MSG, e));
        }
    }

}
