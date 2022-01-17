package co.com.bancolombia.rabbitmq.config;

import co.com.bancolombia.rabbitmq.config.model.RabbitMQConnectionProperties;
import co.com.bancolombia.secretsmanager.SecretsHelper;
import co.com.bancolombia.secretsmanager.api.GenericManager;
import co.com.bancolombia.secretsmanager.api.exceptions.SecretException;
import com.rabbitmq.client.ConnectionFactory;
import org.reactivecommons.async.impl.config.ConnectionFactoryProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

@Configuration
public class RabbitMQConfigHelper extends SecretsHelper<RabbitMQConnectionProperties, ConnectionFactoryProvider> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQConfigHelper.class.getName());
    private static final String FAIL_MSG = "Error creating ConnectionFactoryProvider in email management";

    protected RabbitMQConfigHelper(@Value("${adapters.secrets-manager.secret-rabbit}") final String secretName) {
        super(RabbitMQConnectionProperties.class, secretName);
        LOGGER.info("secretName " + secretName);
    }

    @Primary
    @Bean
    public ConnectionFactoryProvider getConnectionFactoryProvider(GenericManager manager) throws SecretException {
        LOGGER.info("AASDFDSFDASFDFDF");
        return createConfigFromSecret(manager, this::buildConnectionFactoryProvider);
    }

    private ConnectionFactoryProvider buildConnectionFactoryProvider(RabbitMQConnectionProperties properties) {
        final ConnectionFactory factory = new ConnectionFactory();
        PropertyMapper map = PropertyMapper.get();

        map.from(properties::getHostname).whenNonNull().to(factory::setHost);
        map.from(properties::getPort).to(factory::setPort);
        map.from(properties::getUsername).whenNonNull().to(factory::setUsername);
        map.from(properties::getPassword).whenNonNull().to(factory::setPassword);
        map.from(properties::isSsl).whenTrue().as(isSsl -> factory).to(this::configureSsl);
        return () -> factory;
    }

    private void configureSsl(ConnectionFactory factory) {
        try {
            factory.useSslProtocol();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            LOGGER.error(FAIL_MSG, e);
        }
    }
}
