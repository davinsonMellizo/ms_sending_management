package co.com.bancolombia.s3bucket;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.WebIdentityTokenFileCredentialsProvider;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3AsyncClientBuilder;

import java.net.URI;

@Configuration
@RequiredArgsConstructor
public class S3ConnectionConfig {

    private final S3ConnectionProperties s3ConnectionProperties;

    public S3AsyncClient s3AsyncClient(boolean withEndpoint) {

        S3AsyncClientBuilder s3AsyncClient = S3AsyncClient.builder()
                .region(s3ConnectionProperties.getRegion())
                .credentialsProvider(WebIdentityTokenFileCredentialsProvider.create());
        if (withEndpoint) s3AsyncClient.endpointOverride(URI.create(s3ConnectionProperties.getEndpoint()));
        return s3AsyncClient.build();
    }

    @Bean
    @Profile({"dev", "qa", "pdn"})
    public S3AsyncClient s3AsyncClient() {
        return s3AsyncClient(false);
    }

    @Bean
    @Profile({"local"})
    public S3AsyncClient s3AsyncClientLocal() {
        return s3AsyncClient(true);
    }
}
