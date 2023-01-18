package co.com.bancolombia.client;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.r2dbc.ConnectionFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.dialect.PostgresDialect;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.connection.init.CompositeDatabasePopulator;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;
import org.springframework.r2dbc.core.DatabaseClient;

@Configuration
@EnableR2dbcRepositories(entityOperationsRef = "readerR2dbcEntityOperations",
        basePackages={"co.com.bancolombia.client.reader"})
public class DatabaseReaderConfig {

    @Bean("Reader")
    public ConnectionFactory connectionFactory() {
        return ConnectionFactoryBuilder.withUrl("r2dbc:h2:mem:///testDbCR?options=DB_CLOSE_DELAY=-1").build();
    }

    @Bean
    public R2dbcEntityOperations readerR2dbcEntityOperations(@Qualifier("Reader")ConnectionFactory connectionFactory) {

        var databaseClient = DatabaseClient.create(connectionFactory);
        return new R2dbcEntityTemplate(databaseClient, PostgresDialect.INSTANCE);
    }

    @Bean
    public ConnectionFactoryInitializer initializerReader(@Qualifier("Reader") final ConnectionFactory connectionFactory) {
        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);
        CompositeDatabasePopulator populator = new CompositeDatabasePopulator();
        populator.addPopulators(new ResourceDatabasePopulator(new ClassPathResource("schema.sql")));
        populator.addPopulators(new ResourceDatabasePopulator(new ClassPathResource("data-test.sql")));
        initializer.setDatabasePopulator(populator);

        return initializer;
    }
}
