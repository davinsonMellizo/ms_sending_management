package co.com.bancolombia.config;

import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.spi.ConnectionFactoryOptions;
import io.r2dbc.spi.Option;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;

import static io.r2dbc.spi.ConnectionFactoryOptions.DATABASE;
import static io.r2dbc.spi.ConnectionFactoryOptions.DRIVER;
import static io.r2dbc.spi.ConnectionFactoryOptions.HOST;
import static io.r2dbc.spi.ConnectionFactoryOptions.PASSWORD;
import static io.r2dbc.spi.ConnectionFactoryOptions.PORT;
import static io.r2dbc.spi.ConnectionFactoryOptions.USER;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class PostgreSQLWAdapterConfigTest {

    @InjectMocks
    private PostgreSQLWAdapterConfig adapterConfig;

    @BeforeEach
    public void init() throws NoSuchFieldException, IllegalAccessException {

        final Field initialSize = PostgreSQLWAdapterConfig.class.getDeclaredField("initialSize");
        initialSize.setAccessible(true);
        initialSize.set(adapterConfig, 1);

        final Field maxSize = PostgreSQLWAdapterConfig.class.getDeclaredField("maxSize");
        maxSize.setAccessible(true);
        maxSize.set(adapterConfig, 10);

    }

    @Test
    void initializer() {
        ConnectionFactoryOptions options = buildConnectionWriterConfiguration();
        assertNotNull(adapterConfig.initializer(options));
    }

    @Test
    void getConnectionReadConfig() {
        ConnectionFactoryOptions options = buildConnectionWriterConfiguration();
        ConnectionPool pool = adapterConfig.initializer(options);
        assertNotNull(adapterConfig.writerR2dbcEntityOperations(pool));
    }

    public ConnectionFactoryOptions buildConnectionWriterConfiguration(){

        return ConnectionFactoryOptions.builder()
                .option(DRIVER,"postgresql")
                .option(HOST, "localhost")
                .option(PORT, 1010)
                .option(USER,"userName")
                .option(PASSWORD,"password")
                .option(DATABASE, "alertas")
                .option(Option.valueOf("sslmode"), "disable")
                .option(Option.valueOf("schema"), "alertas")
                .build();
    }




}
