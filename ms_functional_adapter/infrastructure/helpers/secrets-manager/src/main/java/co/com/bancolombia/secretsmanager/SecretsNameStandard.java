package co.com.bancolombia.secretsmanager;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Data
@Component
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "aws.secrets-manager")
public class SecretsNameStandard {

    private String secretRds;
    private String secretRabbit;

    public Mono<String> secretForPostgres() {
        return Mono.just(secretRds);
    }
    public Mono<String> secretForRabbitMQ() {
        System.out.println("rabbit secret ++ " + secretRabbit);
        return Mono.just(secretRabbit);
    }
}