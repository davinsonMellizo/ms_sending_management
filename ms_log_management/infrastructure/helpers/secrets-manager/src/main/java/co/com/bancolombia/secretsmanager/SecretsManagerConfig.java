package co.com.bancolombia.secretsmanager;

import config.AWSSecretsManagerConfig;
import connector.AWSSecretManagerConnector;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@RequiredArgsConstructor
public class SecretsManagerConfig {

    private final SecretsManagerProperties properties;

    @Bean
    @Profile({"dev", "qa", "pdn"})
    public AWSSecretManagerConnector connectionAws() {
        return new AWSSecretManagerConnector(AWSSecretsManagerConfig.builder()
                .cacheSeconds(properties.getCacheTime())
                .cacheSize(properties.getCacheSize())
                .build());
    }

    @Bean
    @Profile("local")
    public AWSSecretManagerConnector connectionLocal(@Value("${cloud.aws.secrets-manager.endpoint}") String endpoint) {
        return new AWSSecretManagerConnector(AWSSecretsManagerConfig.builder()
                .endpoint(endpoint)
                .build());
    }

}
