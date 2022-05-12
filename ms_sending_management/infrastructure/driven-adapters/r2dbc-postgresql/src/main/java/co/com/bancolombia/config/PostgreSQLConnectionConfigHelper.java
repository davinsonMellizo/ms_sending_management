package co.com.bancolombia.config;

import co.com.bancolombia.model.log.LoggerBuilder;
import co.com.bancolombia.secretsmanager.SecretsManager;
import co.com.bancolombia.secretsmanager.SecretsNameStandard;
import io.r2dbc.spi.ConnectionFactoryOptions;
import io.r2dbc.spi.Option;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import static io.r2dbc.spi.ConnectionFactoryOptions.*;

@Configuration
@RequiredArgsConstructor
public class PostgreSQLConnectionConfigHelper {

    private final SecretsManager secretsManager;
    private final SecretsNameStandard secretsNameStandard;
    private final LoggerBuilder logger;


    private PostgresqlConnectionProperties postgresProperties() {
        return secretsNameStandard.secretForPostgres()
                .flatMap(secretName -> secretsManager.getSecret(secretName, PostgresqlConnectionProperties.class))
                .block();
    }
    private PostgresqlConnectionProperties postgresPropertiesRead() {
        return secretsNameStandard.secretForPostgresRead()
                .flatMap(secretName -> secretsManager.getSecret(secretName, PostgresqlConnectionProperties.class))
                .block();
    }

    @Bean
    public ConnectionFactoryOptions buildConnectionWriterConfiguration(@Value("${adapters.postgresql.schema}") String schema){
        PostgresqlConnectionProperties properties =  postgresProperties();
        logger.info("data secret rds:"+properties);
        return ConnectionFactoryOptions.builder()
                .option(DRIVER,"postgresql")
                .option(HOST, properties.getHost())
                .option(PORT, properties.getPort())
                .option(USER,properties.getUsername())
                .option(PASSWORD,properties.getPassword())
                .option(DATABASE, properties.getDbname())
                .option(Option.valueOf("sslmode"), "disable")
                .option(Option.valueOf("schema"), schema)
                .build();

    }

    @Bean
    public ConnectionFactoryOptions buildConnectionReaderConfiguration(@Value("${adapters.postgresql.schema}") String schema){
        PostgresqlConnectionProperties properties =  postgresPropertiesRead();
        logger.info("data secret rds:"+properties);
        return ConnectionFactoryOptions.builder()
                .option(DRIVER,"postgresql")
                .option(HOST, properties.getHost())
                .option(PORT, properties.getPort())
                .option(USER,properties.getUsername())
                .option(PASSWORD,properties.getPassword())
                .option(DATABASE, properties.getDbname())
                .option(Option.valueOf("sslmode"), "disable")
                .option(Option.valueOf("schema"), schema)
                .build();

    }

}

