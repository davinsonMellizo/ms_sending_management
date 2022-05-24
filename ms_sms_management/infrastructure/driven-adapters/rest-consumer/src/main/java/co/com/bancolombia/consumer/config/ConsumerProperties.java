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
@ConfigurationProperties(prefix = "adapters.rest-client")
public class ConsumerProperties {

    private int timeout;
    private Resources resources;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Resources {
        private String endpointMasivSms;
        private String endpointMasivAutToken;
        private String endpointMasivToken;
        private String endpointInalambriaSms;
        private String endpointInalambriaToken;
        private String endpointInalambriaRefreshToken;

    }
}
