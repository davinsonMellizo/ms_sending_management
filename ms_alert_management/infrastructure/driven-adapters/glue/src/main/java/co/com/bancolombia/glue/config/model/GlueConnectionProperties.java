package co.com.bancolombia.glue.config.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "adapters.glue")
public class GlueConnectionProperties {
    private String env;
    private String jobName;
}
