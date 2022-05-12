package co.com.bancolombia.config;

import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.core.DatabaseClient;

import java.time.Duration;
import java.util.concurrent.Flow;

@Configuration
@EnableR2dbcRepositories
@RequiredArgsConstructor
public class PostgreSQLAdapterConfig{

    private final ConnectionFactoryOptions options;
    @Value("${cloud.aws.rds.postgresql.pool.initial}")
    private Integer initialSize;

    @Value("${cloud.aws.rds.postgresql.pool.max}")
    private Integer maxSize;

    @Bean("initializer")
    public ConnectionFactory initializer() {
        return ConnectionFactories.get(options);
    }

}
