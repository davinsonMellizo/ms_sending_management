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
@EnableR2dbcRepositories(entityOperationsRef = "writerR2dbcEntityOperations",
        basePackages={"co.com.bancolombia.newness","co.com.bancolombia.client.writer",
                "co.com.bancolombia.contact.writer"})
public class PostgreSQLAdapterConfig{

    @Bean("Writer")
    public ConnectionFactory initializer(@Qualifier("buildConnectionWriterConfiguration")
                                             final ConnectionFactoryOptions options) {
        return ConnectionFactories.get(options);
    }

    @Bean
    public R2dbcEntityOperations writerR2dbcEntityOperations(@Qualifier("Writer")ConnectionFactory connectionFactory) {

        DatabaseClient databaseClient = DatabaseClient.create(connectionFactory);
        return new R2dbcEntityTemplate(databaseClient, PostgresDialect.INSTANCE);

    }

}
