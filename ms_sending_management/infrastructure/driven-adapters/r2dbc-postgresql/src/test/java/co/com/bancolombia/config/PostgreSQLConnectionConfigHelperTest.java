package co.com.bancolombia.config;

import co.com.bancolombia.d2b.model.secret.SyncSecretVault;
import co.com.bancolombia.model.log.LoggerBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostgreSQLConnectionConfigHelperTest {

    private static final String SECRET_NAME = "secret-example";
    public static final String host = "example.com";
    public static final String database = "database-name";
    public static final String schema = "schema";
    public static final String username = "user2";
    public static final String password = "pass2";
    public static final Integer port = 5432;

    @InjectMocks
    private PostgreSQLConnectionConfigHelper helper;


    @Mock
    private SyncSecretVault secretsManager;
    @Mock
    private LoggerBuilder logger;

    @BeforeEach
    public void init() throws NoSuchFieldException, IllegalAccessException {
        when(secretsManager.getSecret(anyString(), any())).thenReturn(properties());

        final Field secretName = PostgreSQLConnectionConfigHelper.class.getDeclaredField("secretName");
        secretName.setAccessible(true);
        secretName.set(helper, "secretName");


    }

    @Test
    void getConnectionReadConfig() {
        assertNotNull(helper.buildConnectionReaderConfiguration("schema", "localhost"));
    }

    @Test
    void getConnectionWriterConfig() {
        assertNotNull(helper.buildConnectionWriterConfiguration("schema"));
    }

    private PostgresqlConnectionProperties properties() {
        PostgresqlConnectionProperties properties = new PostgresqlConnectionProperties();
        properties.setHost(host);
        properties.setDbname(database);
        properties.setSchema(schema);
        properties.setUsername(username);
        properties.setPassword(password);
        properties.setPort(port);
        return properties;
    }


}
