package co.com.bancolombia.config;

import co.com.bancolombia.secretsmanager.SecretsManager;
import co.com.bancolombia.secretsmanager.SecretsNameStandard;
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class PostgreSQLConnectionConfigHelper {
	private final SecretsManager secretsManager;
	private final SecretsNameStandard secretsNameStandard;

	private PostgresqlConnectionProperties postgresProperties() {
		return secretsNameStandard.secretForPostgres()
				.flatMap(secretName -> secretsManager.getSecret(secretName, PostgresqlConnectionProperties.class))
				.block();
	}

	@Bean
	public PostgresqlConnectionConfiguration getConnectionConfig() {
		PostgresqlConnectionProperties properties = postgresProperties();
		return PostgresqlConnectionConfiguration.builder()
				.host(properties.getHost())
				.port(properties.getPort())
				.database(properties.getDbname())
				.schema(properties.getSchema())
				.username(properties.getUsername())
				.password(properties.getPassword())
				.build();
	}
}
