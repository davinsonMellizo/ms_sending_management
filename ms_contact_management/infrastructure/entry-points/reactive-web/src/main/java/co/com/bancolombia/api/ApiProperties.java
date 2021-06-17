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
    private String saveContacts;
    private String findContacts;
    private String updateContacts;
    private String deleteContacts;
    private String saveClient;
    private String findClient;
    private String updateClient;
    private String deleteClient;
}
