package co.com.bancolombia.dynamo.config;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DynamoDBTablesPopretiesTest {
    private final DynamoDBTablesProperties properties = new DynamoDBTablesProperties();

    public static final Map<String, String> names = Map.of("key", "table-name");

    @Test
    void secretsConnectionProperties() {
        properties.setNamesmap(names);
        assertNotNull(properties);
        assertEquals(names, properties.getNamesmap());
    }
}
