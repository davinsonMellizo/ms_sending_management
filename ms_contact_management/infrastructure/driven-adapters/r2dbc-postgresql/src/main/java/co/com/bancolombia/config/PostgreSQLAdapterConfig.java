package co.com.bancolombia.config;

import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@Configuration
@RequiredArgsConstructor
@EnableR2dbcRepositories
public class PostgreSQLAdapterConfig /*extends AbstractR2dbcConfiguration*/ {

    private final ConnectionFactoryOptions pool;
    @Bean("initializer")
    public ConnectionPool initializer() {
        ConnectionFactory connectionFactory = ConnectionFactories.get(pool);
        ConnectionPoolConfiguration configuration = ConnectionPoolConfiguration.builder(connectionFactory)
                .maxSize(10)
                .initialSize(5)
                .build();

        return new ConnectionPool(configuration);
    }

}
