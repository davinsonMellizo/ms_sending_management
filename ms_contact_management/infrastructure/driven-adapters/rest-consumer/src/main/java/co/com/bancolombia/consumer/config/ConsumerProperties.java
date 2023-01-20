package co.com.bancolombia.consumer.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "adapters.rest-consumer")
public class ConsumerProperties {

    private int timeout;
    private Resources resources;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Resources {
        private String retrieveInfo;
        private String sendAlert;
    }
}
