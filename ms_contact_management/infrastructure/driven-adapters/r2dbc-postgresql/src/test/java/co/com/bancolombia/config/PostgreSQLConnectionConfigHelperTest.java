package co.com.bancolombia.config;

import co.com.bancolombia.secretsmanager.SecretsManager;
import co.com.bancolombia.secretsmanager.SecretsNameStandard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostgreSQLConnectionConfigHelperTest {

    private static final String secretName = "secret-example";
    public static final String host = "example.com";
    public static final String database = "database-name";
    public static final String schema = "schema";
    public static final String username = "user2";
    public static final String password = "pass2";
    public static final Integer port = 5432;

    @InjectMocks
    private PostgreSQLConnectionPool helper;


    public final PostgresqlConnectionProperties properties = new PostgresqlConnectionProperties();

    @Mock
    private SecretsNameStandard secretsNameStandard;

    @Mock
    private SecretsManager secretsManager;

    @BeforeEach
    public void init() {
        properties.setHost(host);
        properties.setDbname(database);
        properties.setSchema(schema);
        properties.setUsername(username);
        properties.setPassword(password);
        properties.setPort(port);
        when(secretsManager.getSecret(secretName, PostgresqlConnectionProperties.class)).thenReturn(Mono.just(properties));
        when(secretsNameStandard.secretForPostgres()).thenReturn(Mono.just(secretName));
    }

    @Test
    public void getConnectionConfig() {
        assertNotNull(helper.getConnectionConfig());
    }

}
