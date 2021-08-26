package co.com.bancolombia.config;

import co.com.bancolombia.secretsmanager.SecretsManager;
import co.com.bancolombia.secretsmanager.SecretsNameStandard;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import io.r2dbc.spi.Option;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.r2dbc.ConnectionFactoryBuilder;
import org.springframework.boot.autoconfigure.r2dbc.EmbeddedDatabaseConnection;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

import static io.r2dbc.spi.ConnectionFactoryOptions.*;

@Configuration
@RequiredArgsConstructor
public class PostgreSQLConnectionConfigHelper {


    /*private PostgresqlConnectionProperties postgresProperties() {
        return secretsNameStandard.secretForPostgres()
                .flatMap(secretName -> secretsManager.getSecret(secretName, PostgresqlConnectionProperties.class))
                .block();
    }

    @Bean
    public ConnectionFactoryOptions buildConnectionConfiguration(){
        PostgresqlConnectionProperties properties =  postgresProperties();
        return ConnectionFactoryOptions.builder()
                .option(DRIVER,"postgresql")
                .option(HOST, properties.getHost())
                .option(PORT, properties.getPort())
                .option(USER,properties.getUsername())
                .option(PASSWORD,properties.getPassword())
                .option(DATABASE, properties.getDbname())
                .option(Option.valueOf("sslmode"), "disable")
                .option(Option.valueOf("schema"), "schalertd")
                .build();

    }*/

    private final SecretsManager secretsManager;
    private final SecretsNameStandard secretsNameStandard;

    public static final String CONN_BASE = "r2dbc:pool:postgres://";
    public static final String INITIAL_SIZE_FORMAT = "?initialSize=%s";
    public static final String MAX_SIZE_FORMAT = "&maxSize=%s";
    public static final String COLON = ":";
    public static final String SLASH = "/";



    private final ResourceLoader resourceLoader;

    private PostgresqlConnectionProperties postgresProperties() {
        return secretsNameStandard.secretForPostgres()
                .flatMap(secretName -> secretsManager.getSecret(secretName, PostgresqlConnectionProperties.class))
                .block();
    }

    @Bean
    public ConnectionFactory buildConnectionConfiguration() {
        PostgresqlConnectionProperties properties = postgresProperties();
        R2dbcProperties props = new R2dbcProperties();
        props.setUrl(buildConnectionString(properties));
        props.setUsername(properties.getUsername());
        props.setPassword(properties.getPassword());
        props.getProperties().put("sslmode", "disable");
        props.getProperties().put("schema", "schalertd");
        return ConnectionFactoryBuilder.of(props, () -> EmbeddedDatabaseConnection.get(resourceLoader.getClassLoader()))
                .configure((options) -> {
                }).build();
    }

    private String buildConnectionString(PostgresqlConnectionProperties properties) {
        return new StringBuilder()
                .append(CONN_BASE)
                .append(properties.getHost())
                .append(COLON)
                .append(properties.getPort())
                .append(SLASH)
                .append(properties.getDbname())
                .append(String.format(INITIAL_SIZE_FORMAT, 50))
                .append(String.format(MAX_SIZE_FORMAT, 100))
                .toString();
    }
}

