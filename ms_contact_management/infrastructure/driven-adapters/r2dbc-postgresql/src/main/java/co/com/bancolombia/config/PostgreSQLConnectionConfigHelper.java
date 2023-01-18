package co.com.bancolombia.config;

import co.com.bancolombia.d2b.model.secret.SyncSecretVault;
import co.com.bancolombia.log.LoggerBuilder;
import io.r2dbc.spi.ConnectionFactoryOptions;
import io.r2dbc.spi.Option;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.r2dbc.pool.PoolingConnectionFactoryProvider.MAX_SIZE;
import static io.r2dbc.spi.ConnectionFactoryOptions.DATABASE;
import static io.r2dbc.spi.ConnectionFactoryOptions.DRIVER;
import static io.r2dbc.spi.ConnectionFactoryOptions.HOST;
import static io.r2dbc.spi.ConnectionFactoryOptions.PASSWORD;
import static io.r2dbc.spi.ConnectionFactoryOptions.PORT;
import static io.r2dbc.spi.ConnectionFactoryOptions.PROTOCOL;
import static io.r2dbc.spi.ConnectionFactoryOptions.USER;

@Configuration
@RequiredArgsConstructor
public class PostgreSQLConnectionConfigHelper {

    private final SyncSecretVault syncSecretVault;

    private final LoggerBuilder logger;
    @Value("${adapters.secrets-manager.secret-rds}")
    private String secretName;



    private PostgresqlConnectionProperties postgresProperties() {
        return syncSecretVault.getSecret(secretName, PostgresqlConnectionProperties.class);
    }

    @Bean
    public ConnectionFactoryOptions buildConnectionWriterConfiguration(
            @Value("${cloud.aws.rds.postgresql.schema}") String schema,
            @Value("${cloud.aws.rds.postgresql.pool.max}") Integer max){

        PostgresqlConnectionProperties properties =  postgresProperties();
        logger.info("data secret rds:"+properties);
        return ConnectionFactoryOptions.builder()
                .option(MAX_SIZE, max)
                .option(DRIVER,"pool")
                .option(PROTOCOL, "postgresql")
                .option(HOST, properties.getHost())
                .option(PORT, properties.getPort())
                .option(USER,properties.getUsername())
                .option(PASSWORD,properties.getPassword())
                .option(DATABASE, properties.getDbname())
                .option(Option.valueOf("sslmode"), "verify-full")
                .option(Option.valueOf("schema"), schema)
                .build();
    }

    @Bean
    public ConnectionFactoryOptions buildConnectionReaderConfiguration(
            @Value("${cloud.aws.rds.postgresql.schema}") String schema,
            @Value("${cloud.aws.rds.postgresql.hostRead}") String hostRead,
            @Value("${cloud.aws.rds.postgresql.pool.max}") Integer max){

        PostgresqlConnectionProperties properties =  postgresProperties();
        return ConnectionFactoryOptions.builder()
                .option(MAX_SIZE, max)
                .option(DRIVER,"pool")
                .option(PROTOCOL, "postgresql")
                .option(HOST, hostRead)
                .option(PORT, properties.getPort())
                .option(USER,properties.getUsername())
                .option(PASSWORD,properties.getPassword())
                .option(DATABASE, properties.getDbname())
                .option(Option.valueOf("sslmode"), "verify-full")
                .option(Option.valueOf("schema"), schema)
                .build();
    }

}

