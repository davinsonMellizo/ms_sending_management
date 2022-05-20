package co.com.bancolombia.config;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.dialect.PostgresDialect;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.core.DatabaseClient;

@Configuration
@RequiredArgsConstructor
@EnableR2dbcRepositories(entityOperationsRef = "readerR2dbcEntityOperations",
        basePackages={"co.com.bancolombia.consumer","co.com.bancolombia.client.reader",
                "co.com.bancolombia.contact.reader", "co.com.bancolombia.state", "co.com.bancolombia.document",
                "co.com.bancolombia.contactmedium"
        })
public class PostgreSQLReaderAdapterConfig {

    @Bean("Reader")
    public ConnectionFactory initializer(@Qualifier("buildConnectionReaderConfiguration")
                                             final ConnectionFactoryOptions options) {
        return ConnectionFactories.get(options);
    }

    @Bean
    public R2dbcEntityOperations readerR2dbcEntityOperations(@Qualifier("Reader")ConnectionFactory connectionFactory) {

        DatabaseClient databaseClient = DatabaseClient.create(connectionFactory);
        return new R2dbcEntityTemplate(databaseClient, PostgresDialect.INSTANCE);
    }

}
