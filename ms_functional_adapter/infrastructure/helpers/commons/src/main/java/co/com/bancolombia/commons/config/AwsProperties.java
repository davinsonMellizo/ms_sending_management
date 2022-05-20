package co.com.bancolombia.commons.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "aws")
public class AwsProperties {
    private String region;
    protected S3 s3;

    @Data
    @Component
    @AllArgsConstructor
    @NoArgsConstructor
    public static class S3 {
        private String endpoint;
        private String bucket;
        private String configListenerMqKey;
    }
}