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
@ConfigurationProperties(prefix = "adapters.secrets-manager")
public class SecretsNameStandard {

    private String secretRds;
    private String secretRabbit;
    private String secretRetrieve;

    public Mono<String> secretForPostgres() {
        return Mono.just(secretRds);
    }

    public Mono<String> secretForRabbitMQ() {
        return Mono.just(secretRabbit);
    }
    public Mono<String> secretForRetrieve() {
        return Mono.just(secretRetrieve);
    }
}