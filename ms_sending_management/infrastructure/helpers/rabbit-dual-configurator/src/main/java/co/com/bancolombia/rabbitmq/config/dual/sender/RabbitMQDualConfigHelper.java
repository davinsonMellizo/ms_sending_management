package co.com.bancolombia.rabbitmq.config.dual.sender;

import co.com.bancolombia.d2b.model.secret.SyncSecretVault;
import co.com.bancolombia.model.log.LoggerBuilder;
import co.com.bancolombia.rabbitmq.config.model.RabbitMQConnectionProperties;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import org.reactivecommons.async.impl.communications.ReactiveMessageSender;
import org.reactivecommons.async.impl.communications.TopologyCreator;
import org.reactivecommons.async.impl.config.BrokerConfig;
import org.reactivecommons.async.impl.config.ConnectionFactoryProvider;
import org.reactivecommons.async.impl.config.RabbitProperties;
import org.reactivecommons.async.impl.config.props.BrokerConfigProps;
import org.reactivecommons.async.impl.converters.MessageConverter;
import org.reactivecommons.async.impl.reply.ReactiveReplyRouter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.ChannelPool;
import reactor.rabbitmq.ChannelPoolFactory;
import reactor.rabbitmq.ChannelPoolOptions;
import reactor.rabbitmq.RabbitFlux;
import reactor.rabbitmq.Sender;
import reactor.rabbitmq.SenderOptions;
import reactor.rabbitmq.Utils;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import static co.com.bancolombia.rabbitmq.config.dual.commons.Utils.createConnectionMono;

@Configuration
@RequiredArgsConstructor
public class RabbitMQDualConfigHelper {
    private final LoggerBuilder logger;
    private static final String SENDER_TYPE = "sender";
    private final BrokerConfigProps props;
    private final SyncSecretVault secretsManager;
    private static final String TLS = "TLSv1.2";
    private static final String FAIL_MSG = "Error creating ConnectionFactoryProvider";

    @Value("${spring.application.name}")
    private String appName;
    @Value("${adapters.secrets-manager.secret-rabbit-dual}")
    private String secretName;

    @Bean("rabbitDirectAsyncGatewayDual")
    public RabbitDirectAsyncGateway rabbitDirectAsyncGatewayDual(BrokerConfig config, ReactiveReplyRouter router,
                                                             BrokerConfigProps brokerConfigProps,
                                                             RabbitProperties rabbitProperties,
                                                             ConnectionFactory factory,
                                                             MessageConverter converter){
        final ConnectionFactoryProvider provider = () -> factory;
        SenderOptions senderOptions =  reactiveCommonsSenderOptions(provider,rabbitProperties);
        ReactiveMessageSender sender = messageSender(converter, brokerConfigProps, senderOptions);

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

    public ReactiveMessageSender messageSender(MessageConverter converter, BrokerConfigProps brokerConfigProps, SenderOptions senderOptions) {
        final Sender sender = RabbitFlux.createSender(senderOptions);
        return new ReactiveMessageSender(sender, brokerConfigProps.getAppName(), converter, new TopologyCreator(sender));
    }

    public SenderOptions reactiveCommonsSenderOptions(ConnectionFactoryProvider provider, RabbitProperties rabbitProperties) {
        final Mono<Connection> senderConnection =
                createConnectionMono(provider.getConnectionFactory(), appName, SENDER_TYPE, logger);
        final ChannelPoolOptions channelPoolOptions = new ChannelPoolOptions();
        final PropertyMapper map = PropertyMapper.get();

        map.from(rabbitProperties.getCache().getChannel()::getSize).whenNonNull()
                .to(channelPoolOptions::maxCacheSize);

        final ChannelPool channelPool = ChannelPoolFactory.createChannelPool(
                senderConnection,
                channelPoolOptions
        );

        return new SenderOptions()
                .channelPool(channelPool)
                .resourceManagementChannelMono(channelPool.getChannelMono()
                        .transform(Utils::cache));
    }


}
