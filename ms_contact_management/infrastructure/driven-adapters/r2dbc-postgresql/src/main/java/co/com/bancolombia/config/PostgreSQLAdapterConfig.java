package co.com.bancolombia.config;

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@Configuration
@RequiredArgsConstructor
@EnableR2dbcRepositories
public class PostgreSQLAdapterConfig extends AbstractR2dbcConfiguration {

    private final PostgresqlConnectionConfiguration connectionConfiguration;

    @Bean("connect")
    @Override
    public ConnectionFactory connectionFactory() {
        return new PostgresqlConnectionFactory(connectionConfiguration);
    }

}
