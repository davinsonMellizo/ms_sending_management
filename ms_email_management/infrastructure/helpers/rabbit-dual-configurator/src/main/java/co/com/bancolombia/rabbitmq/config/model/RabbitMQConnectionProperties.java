package co.com.bancolombia.rabbitmq.config.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@AllArgsConstructor
@RequiredArgsConstructor
public class RabbitMQConnectionProperties {

    String virtualhost;
    String hostname;
    String password;
    String username;
    boolean ssl;
    Integer port;

}
