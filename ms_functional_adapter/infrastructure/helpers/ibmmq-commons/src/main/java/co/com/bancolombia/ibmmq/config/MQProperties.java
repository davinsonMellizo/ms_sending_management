package co.com.bancolombia.ibmmq.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@AllArgsConstructor
@NoArgsConstructor
public class MQProperties {

    private String user;
    private String password;
    private String trustStorePass;

}