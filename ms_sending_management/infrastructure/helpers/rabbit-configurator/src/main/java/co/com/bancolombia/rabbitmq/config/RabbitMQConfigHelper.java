package co.com.bancolombia.rabbitmq.config;

import co.com.bancolombia.model.log.LoggerBuilder;
import co.com.bancolombia.rabbitmq.config.model.RabbitMQConnectionProperties;
import co.com.bancolombia.secretsmanager.SecretsManager;
import co.com.bancolombia.secretsmanager.SecretsNameStandard;
import com.rabbitmq.client.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import org.reactivecommons.async.impl.config.ConnectionFactoryProvider;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

@Configuration
@RequiredArgsConstructor
public class RabbitMQConfigHelper{
    private final LoggerBuilder logger;
    private final SecretsManager secretsManager;
    private final SecretsNameStandard secretsNameStandard;
    private static final String FAIL_MSG = "Error creating ConnectionFactoryProvider in Security_Filters";


    private RabbitMQConnectionProperties rabbitProperties() {
        return secretsNameStandard.secretForRabbitMQ()
                .flatMap(secretName -> secretsManager.getSecret(secretName, RabbitMQConnectionProperties.class))
                .block();
    }

    @Primary
    @Bean
    public ConnectionFactoryProvider getConnectionFactoryProvider(){
        RabbitMQConnectionProperties properties = rabbitProperties();
        final ConnectionFactory factory = new ConnectionFactory();
        PropertyMapper map = PropertyMapper.get();

        map.from(properties::getHostname).whenNonNull().to(factory::setHost);
        map.from(properties::getPort).to(factory::setPort);
        map.from(properties::getUsername).whenNonNull().to(factory::setUsername);
        map.from(properties::getPassword).whenNonNull().to(factory::setPassword);
        //map.from(properties::isSsl).whenTrue().as(isSsl -> factory).to(this::configureSsl);
        map.from(false).whenTrue().as(isSsl -> factory).to(this::configureSsl);

        return () -> factory;
    }

    private void configureSsl(ConnectionFactory factory) {
        try {
            factory.useSslProtocol();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            logger.info(String.format(FAIL_MSG, e));
        }
    }
}
