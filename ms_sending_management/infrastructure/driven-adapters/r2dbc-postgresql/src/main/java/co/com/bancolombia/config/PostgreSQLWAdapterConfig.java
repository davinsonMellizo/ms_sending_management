package co.com.bancolombia.config;

import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.core.DefaultReactiveDataAccessStrategy;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.dialect.PostgresDialect;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.core.DatabaseClient;

import java.time.Duration;

@Configuration
@EnableR2dbcRepositories(entityOperationsRef = "writerR2dbcEntityOperations", basePackages={"co.com.bancolombia.alertclient"})
@RequiredArgsConstructor
public class PostgreSQLWAdapterConfig {

    @Value("${adapters.postgresql.pool.initial}")
    private Integer initialSize;

    @Value("${adapters.postgresql.pool.max}")
    private Integer maxSize;

    @Bean("Writer")
    public ConnectionPool initializer(@Qualifier("buildConnectionWriterConfiguration")
                                          final ConnectionFactoryOptions pool) {
        ConnectionFactory connectionFactory = ConnectionFactories.get(pool);
        ConnectionPoolConfiguration configuration = ConnectionPoolConfiguration.builder(connectionFactory)
                .maxIdleTime(Duration.ofMillis(1000))
                .maxSize(maxSize)
                .initialSize(initialSize)
                .build();

        return new ConnectionPool(configuration);
    }

    @Bean
    public R2dbcEntityOperations writerR2dbcEntityOperations(@Qualifier("Writer")ConnectionPool connectionPool) {

        DatabaseClient databaseClient = DatabaseClient.create(connectionPool);
        return new R2dbcEntityTemplate(databaseClient, PostgresDialect.INSTANCE);
    }

}
