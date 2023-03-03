package co.com.bancolombia.config;

import co.com.bancolombia.d2b.model.secret.SyncSecretVault;
import co.com.bancolombia.log.LoggerBuilder;
import co.com.bancolombia.secretsmanager.api.exceptions.SecretException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostgreSQLConnectionConfigHelperTest {

    public static final String host = "example.com";
    public static final String database = "database-name";
    public static final String schema = "schema";
    public static final String username = "user2";
    public static final String password = "pass2";
    public static final Integer port = 5432;

    @InjectMocks
    private PostgreSQLConnectionConfigHelper helper;

    public final PostgresqlConnectionProperties properties = new PostgresqlConnectionProperties();

    @Mock
    private SyncSecretVault secretsManager;
    @Mock
    private LoggerBuilder logger;

    @BeforeEach
    public void init() {
        properties.setHost(host);
        properties.setDbname(database);
        properties.setSchema(schema);
        properties.setUsername(username);
        properties.setPassword(password);
        properties.setPort(port);
        when(secretsManager.getSecret(any(), any())).thenReturn(properties);
    }

    @Test
    void getConnectionReadConfig() throws SecretException {
        assertNotNull(helper.buildConnectionReaderConfiguration("schema","host", 1));
    }

    @Test
    void getConnectionWriterConfig() throws SecretException {
        assertNotNull(helper.buildConnectionWriterConfiguration("schema",1));
    }

}

