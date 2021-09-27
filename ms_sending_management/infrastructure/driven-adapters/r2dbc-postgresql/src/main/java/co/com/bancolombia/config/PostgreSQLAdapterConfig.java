package co.com.bancolombia.config;

import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

import java.time.Duration;

@Configuration
@EnableR2dbcRepositories
@RequiredArgsConstructor
public class PostgreSQLAdapterConfig{

    private final ConnectionFactoryOptions pool;
    @Value("${adapters.postgresql.pool.initial}")
    private Integer initialSize;

    @Value("${adapters.postgresql.pool.max}")
    private Integer maxSize;

    @Bean("initializer")
    public ConnectionPool initializer() {
        ConnectionFactory connectionFactory = ConnectionFactories.get(pool);
        ConnectionPoolConfiguration configuration = ConnectionPoolConfiguration.builder(connectionFactory)
                .maxIdleTime(Duration.ofMillis(1000))
                .maxSize(maxSize)
                .initialSize(initialSize)
                .build();

        return new ConnectionPool(configuration);
    }

}
