package co.com.bancolombia.config;

import java.time.Duration;

import co.com.bancolombia.secretsmanager.SecretsManager;
import co.com.bancolombia.secretsmanager.SecretsNameStandard;
import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class PostgreSQLConnectionPool {
	@Value("${cloud.aws.rds.postgresql.pool.initial}")
    private int INITIAL_SIZE = 1;
	@Value("${cloud.aws.rds.postgresql.pool.max}")
    private int MAX_SIZE =1;
    private static final int MAX_IDLE_TIME = 30;
	@Value("${cloud.aws.rds.postgresql.schema}")
    private String SCHEMA;


	private final SecretsManager secretsManager;
	private final SecretsNameStandard secretsNameStandard;

	private PostgresqlConnectionProperties postgresProperties() {
		return secretsNameStandard.secretForPostgres()
				.flatMap(secretName -> secretsManager.getSecret(secretName, PostgresqlConnectionProperties.class))
				.block();
	}

	@Bean
	public ConnectionPool getConnectionConfig() {
		PostgresqlConnectionProperties pgProperties = postgresProperties();
		pgProperties.setSchema(SCHEMA);

		return buildConnectionConfiguration(pgProperties);
	}

	private ConnectionPool buildConnectionConfiguration(PostgresqlConnectionProperties properties) {
		PostgresqlConnectionConfiguration dbConfiguration = PostgresqlConnectionConfiguration.builder()
				.host(properties.getHost())
				.port(properties.getPort())
				.database(properties.getDbname())
				.schema(properties.getSchema())
				.username(properties.getUsername())
				.password(properties.getPassword())
				.build();

        ConnectionPoolConfiguration poolConfiguration = ConnectionPoolConfiguration.builder()
                .connectionFactory(new PostgresqlConnectionFactory(dbConfiguration))
                .name("api-postgres-connection-pool")
                .initialSize(INITIAL_SIZE)
                .maxSize(MAX_SIZE)
                .maxIdleTime(Duration.ofMinutes(MAX_IDLE_TIME))
                .validationQuery("SELECT 1")
                .build();

		return new ConnectionPool(poolConfiguration);
	}
}
