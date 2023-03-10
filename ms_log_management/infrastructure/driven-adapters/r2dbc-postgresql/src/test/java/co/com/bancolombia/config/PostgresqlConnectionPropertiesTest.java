package co.com.bancolombia.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PostgresqlConnectionPropertiesTest {

    private PostgresqlConnectionProperties properties;

    public static final String database = "database-name";
    public static final String schema = "schema";
    public static final String username = "user1";
    public static final String password = "pass1";
    public static final String host = "example.com";
    public static final Integer port = 5432;

    @Test
    void propertiesPlainObject() {
        properties = new PostgresqlConnectionProperties();
        properties.setHost(host);
        properties.setDbname(database);
        properties.setSchema(schema);
        properties.setUsername(username);
        properties.setPassword(password);
        properties.setPort(port);

        assertEquals(host, properties.getHost());
        assertEquals(database, properties.getDbname());
        assertEquals(schema, properties.getSchema());
        assertEquals(username, properties.getUsername());
        assertEquals(password, properties.getPassword());
        assertEquals(port, properties.getPort());
    }
}
