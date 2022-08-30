package co.com.bancolombia.dynamo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Data
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "adapters.repositories.tables")
public class DynamoDBTablesProperties {

    private Map<String, String> namesmap;

}
