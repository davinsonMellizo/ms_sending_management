package co.com.bancolombia.consumer.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "adapters.restconsumer")
public class RestConsumerProperties {
    private int timeout;
    private Resources resources;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Resources {
        private String endpointCampaign;
    }
}
