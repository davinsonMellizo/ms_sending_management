package co.com.bancolombia.config;

import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactoryOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.dialect.PostgresDialect;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.core.DatabaseClient;

import java.time.Duration;

@Configuration
@EnableR2dbcRepositories(entityOperationsRef = "readerR2dbcEntityOperations",
        basePackages={"co.com.bancolombia.remitter","co.com.bancolombia.provider",
                "co.com.bancolombia.priority","co.com.bancolombia.document",
                "co.com.bancolombia.contact","co.com.bancolombia.consumer",
                "co.com.bancolombia.client","co.com.bancolombia.category",
                "co.com.bancolombia.alert","co.com.bancolombia.alerttransaction"
        })
@RequiredArgsConstructor
public class PostgreSQLAdapterConfig{
    private static final Integer MAX_IDLE_TIME = 1000;

    @Value("${adapters.postgresql.pool.initial}")
    private Integer initialSize;

    @Value("${adapters.postgresql.pool.max}")
    private Integer maxSize;

    @Bean("Reader")
    public ConnectionPool initializer(@Qualifier("buildConnectionReaderConfiguration")
                                          final ConnectionFactoryOptions pool) {
        var connectionFactory = ConnectionFactories.get(pool);
        ConnectionPoolConfiguration configuration = ConnectionPoolConfiguration.builder(connectionFactory)
                .maxIdleTime(Duration.ofMillis(MAX_IDLE_TIME))
                .maxSize(maxSize)
                .initialSize(initialSize)
                .build();

        return new ConnectionPool(configuration);
    }

    @Bean
    public R2dbcEntityOperations readerR2dbcEntityOperations(@Qualifier("Reader")ConnectionPool connectionPool) {

        var databaseClient = DatabaseClient.create(connectionPool);
        return new R2dbcEntityTemplate(databaseClient, PostgresDialect.INSTANCE);
    }

}
