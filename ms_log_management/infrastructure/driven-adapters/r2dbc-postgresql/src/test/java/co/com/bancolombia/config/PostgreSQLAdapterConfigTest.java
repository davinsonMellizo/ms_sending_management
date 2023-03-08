package co.com.bancolombia.config;

import io.r2dbc.spi.ConnectionFactoryOptions;
import io.r2dbc.spi.Option;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.r2dbc.spi.ConnectionFactoryOptions.DATABASE;
import static io.r2dbc.spi.ConnectionFactoryOptions.DRIVER;
import static io.r2dbc.spi.ConnectionFactoryOptions.HOST;
import static io.r2dbc.spi.ConnectionFactoryOptions.PASSWORD;
import static io.r2dbc.spi.ConnectionFactoryOptions.PORT;
import static io.r2dbc.spi.ConnectionFactoryOptions.USER;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class PostgreSQLAdapterConfigTest {
    @InjectMocks
    private PostgreSQLAdapterConfig config;
    @Test
    void readerR2dbcEntityOperations() {
        ConnectionFactoryOptions options = buildConnectionWriterConfiguration();
        assertNotNull(config.initializer(options));
    }

    @Test
    void initializer() {
        ConnectionFactoryOptions options = buildConnectionWriterConfiguration();
        assertNotNull(config.initializer(options));
    }

    public ConnectionFactoryOptions buildConnectionWriterConfiguration() {

        return ConnectionFactoryOptions.builder()
                .option(DRIVER, "postgresql")
                .option(HOST, "localhost")
                .option(PORT, 1010)
                .option(USER, "userName")
                .option(PASSWORD, "password")
                .option(DATABASE, "alertas")
                .option(Option.valueOf("sslmode"), "disable")
                .option(Option.valueOf("schema"), "alertas")
                .build();
    }

}
