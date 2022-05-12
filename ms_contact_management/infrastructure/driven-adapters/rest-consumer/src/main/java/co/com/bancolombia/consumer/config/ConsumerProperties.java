package co.com.bancolombia.consumer.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    private String clientId;
    private String clientSecret;
    private String messageId;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Resources {
        private String basicKit;
        private String retrieveInfo;
    }
}