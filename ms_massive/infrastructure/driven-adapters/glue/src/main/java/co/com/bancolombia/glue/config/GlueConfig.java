package co.com.bancolombia.glue.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.WebIdentityTokenFileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.glue.GlueAsyncClient;

@Configuration
@RequiredArgsConstructor
public class GlueConfig {

    private static final Region REGION = Region.US_EAST_1;

    @Bean
    public GlueAsyncClient glueClientConfig() {
        return GlueAsyncClient.builder()
                .region(REGION)
                .credentialsProvider(WebIdentityTokenFileCredentialsProvider.create())
                .build();
    }

}
