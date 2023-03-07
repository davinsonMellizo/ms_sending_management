package co.com.bancolombia.config;

import co.com.bancolombia.d2b.model.secret.SyncSecretVault;
import co.com.bancolombia.model.log.LoggerBuilder;
import io.r2dbc.spi.ConnectionFactoryOptions;
import io.r2dbc.spi.Option;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.r2dbc.spi.ConnectionFactoryOptions.DATABASE;
import static io.r2dbc.spi.ConnectionFactoryOptions.DRIVER;
import static io.r2dbc.spi.ConnectionFactoryOptions.HOST;
import static io.r2dbc.spi.ConnectionFactoryOptions.PASSWORD;
import static io.r2dbc.spi.ConnectionFactoryOptions.PORT;
import static io.r2dbc.spi.ConnectionFactoryOptions.USER;

@Configuration
@RequiredArgsConstructor
public class PostgreSQLConnectionConfigHelper {

    private final LoggerBuilder logger;
    private final SyncSecretVault secretsManager;
    private static final String SCHEMA_NAME = "${adapters.postgresql.schema}";
    private static final String HOST_NAME = "${adapters.postgresql.hostRead}";

    @Value("${adapters.secrets-manager.secret-rds}")
    private String secretName;


    private PostgresqlConnectionProperties postgresProperties() {
        return secretsManager.getSecret(secretName, PostgresqlConnectionProperties.class);
    }

    @Bean
    public ConnectionFactoryOptions buildConnectionWriterConfiguration(@Value(SCHEMA_NAME) String schema) {
        PostgresqlConnectionProperties properties = postgresProperties();
        logger.info("data secret rds:" + properties);
        return ConnectionFactoryOptions.builder()
                .option(DRIVER, "postgresql")
                .option(HOST, properties.getHost())
                .option(PORT, properties.getPort())
                .option(USER, properties.getUsername())
                .option(PASSWORD, properties.getPassword())
                .option(DATABASE, properties.getDbname())
                .option(Option.valueOf("sslmode"), "disable")
                .option(Option.valueOf("schema"), schema)
                .build();
    }

    @Bean
    public ConnectionFactoryOptions buildConnectionReaderConfiguration(@Value(SCHEMA_NAME) String schema,
                                                                       @Value(HOST_NAME) String hostRead) {
        PostgresqlConnectionProperties properties = postgresProperties();
        return ConnectionFactoryOptions.builder()
                .option(DRIVER, "postgresql")
                .option(HOST, hostRead)
                .option(PORT, properties.getPort())
                .option(USER, properties.getUsername())
                .option(PASSWORD, properties.getPassword())
                .option(DATABASE, properties.getDbname())
                .option(Option.valueOf("sslmode"), "disable")
                .option(Option.valueOf("schema"), schema)
                .build();
    }

}

