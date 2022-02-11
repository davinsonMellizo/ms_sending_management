package co.com.bancolombia.ses.config;

import co.com.bancolombia.model.log.LoggerBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesAsyncClient;


@RequiredArgsConstructor
@Configuration
public class SesConfig {

    private static final Region region = Region.US_EAST_1;
    private final LoggerBuilder logger;

    @Bean
    public SesAsyncClient sesClientConfig() {
        try {
            return SesAsyncClient.builder()
                    .region(region)
                    .credentialsProvider(getProviderChain())
                    .build();

        } catch (IllegalStateException | ExceptionInInitializerError ex) {
            logger.info("exception " + ex);
            logger.info("Error al inicializar cliente ses".concat(ex.getMessage()));
        }
        return null;
    }

    public AwsCredentialsProviderChain getProviderChain() {
        return AwsCredentialsProviderChain.builder()
                .addCredentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .addCredentialsProvider(SystemPropertyCredentialsProvider.create())
                .addCredentialsProvider(WebIdentityTokenFileCredentialsProvider.create())
                .addCredentialsProvider(ProfileCredentialsProvider.create())
                .addCredentialsProvider(ContainerCredentialsProvider.builder().build())
                .addCredentialsProvider(InstanceProfileCredentialsProvider.create())
                .build();
    }

}
