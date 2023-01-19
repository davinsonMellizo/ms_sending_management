package co.com.bancolombia.glueclient.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "cloud.aws.glue")
public class AwsGlueProperties {
    private String endpoint;
    private String region;
}