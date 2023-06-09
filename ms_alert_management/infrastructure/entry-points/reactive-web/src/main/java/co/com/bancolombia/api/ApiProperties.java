package co.com.bancolombia.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "adapters.entries.reactive-web")
public class ApiProperties {
    private String alert;
    private String campaign;
    private String category;
    private String consumer;
    private String pathBase;
    private String priority;
    private String provider;
    private String remitter;
    private String send;
    private String service;
    private String schedule;

}
