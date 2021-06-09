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
@ConfigurationProperties(prefix = "entries.reactive-web")
public class ApiProperties {
    private String pathBase;
    private String saveAlert;
    private String findAlert;
    private String updateAlert;
    private String deleteAlert;
}
