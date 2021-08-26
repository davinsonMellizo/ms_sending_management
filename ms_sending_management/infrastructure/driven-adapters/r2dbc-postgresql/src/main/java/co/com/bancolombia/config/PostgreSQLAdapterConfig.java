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
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;

import java.time.Duration;

@Configuration
@EnableR2dbcRepositories
@RequiredArgsConstructor
public class PostgreSQLAdapterConfig{

    @Bean("initializer")
    public ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {

        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);

        return initializer;
    }
    /*private final ConnectionFactoryOptions pool;
    @Bean("initializer")
    public ConnectionPool initializer() {
        ConnectionFactory connectionFactory = ConnectionFactories.get(pool);
        ConnectionPoolConfiguration configuration = ConnectionPoolConfiguration.builder(connectionFactory)
                .maxIdleTime(Duration.ofMillis(1000))
                .maxSize(100)
                .initialSize(50)
                .build();

        return new ConnectionPool(configuration);
    }*/

}
