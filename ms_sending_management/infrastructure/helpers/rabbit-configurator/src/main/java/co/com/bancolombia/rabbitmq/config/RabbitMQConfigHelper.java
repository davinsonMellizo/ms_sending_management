package co.com.bancolombia.rabbitmq.config;

import co.com.bancolombia.d2b.model.secret.SyncSecretVault;
import co.com.bancolombia.model.log.LoggerBuilder;
import co.com.bancolombia.rabbitmq.config.model.RabbitMQConnectionProperties;
import com.rabbitmq.client.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import org.reactivecommons.async.rabbit.config.ConnectionFactoryProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

@Configuration
@RequiredArgsConstructor
public class RabbitMQConfigHelper {
    private final LoggerBuilder logger;
    private final SyncSecretVault secretsManager;
    private static final String TLS = "TLSv1.2";
    private static final String FAIL_MSG = "Error creating ConnectionFactoryProvider";
    @Value("${adapters.secrets-manager.secret-rabbit}")
    private String secretName;


    private RabbitMQConnectionProperties rabbitProperties() {
        return secretsManager.getSecret(secretName, RabbitMQConnectionProperties.class);
    }

    @Bean
    @Primary
    public ConnectionFactoryProvider getConnectionFactoryProvider() {
        RabbitMQConnectionProperties properties = rabbitProperties();

        final var factory = new ConnectionFactory();
        var map = PropertyMapper.get();

        map.from(properties::getHostname).whenNonNull().to(factory::setHost);
        map.from(properties::getPort).to(factory::setPort);
        map.from(properties::getUsername).whenNonNull().to(factory::setUsername);
        map.from(properties::getPassword).whenNonNull().to(factory::setPassword);
        map.from(properties::isSsl).whenTrue().as(isSsl -> factory).to(this::configureSsl);

        return () -> factory;
    }

    private void configureSsl(ConnectionFactory factory) {
        try {
            var context = SSLContext.getInstance(TLS);
            context.init(null, null, null);

            factory.useSslProtocol(context);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            logger.info(String.format(FAIL_MSG, e));
        }
    }

}
