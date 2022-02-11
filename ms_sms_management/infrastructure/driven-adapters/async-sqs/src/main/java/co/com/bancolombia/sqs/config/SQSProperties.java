package co.com.bancolombia.sqs.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "adapters.async-sqs")
public class SQSProperties {
    private String url;
    private int timeout;
    private int poolSize;
    private String regionAws;
}
