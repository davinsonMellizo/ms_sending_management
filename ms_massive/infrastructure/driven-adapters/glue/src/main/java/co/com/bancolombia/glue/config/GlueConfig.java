package co.com.bancolombia.glue.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.glue.GlueAsyncClient;

@Configuration
@Profile({"local", "dev", "qa", "pdn"})
@RequiredArgsConstructor
public class GlueConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlueConfig.class);
    private static final Region region = Region.US_EAST_1;

    @Bean
    public GlueAsyncClient glueClientConfig() {
        try {
            return GlueAsyncClient.builder()
                    .region(region)
                    .credentialsProvider(getProviderChain())
                    .build();

        } catch (IllegalStateException | ExceptionInInitializerError ex) {
            LOGGER.info("Exception", ex);
            LOGGER.error("Error al inicializar cliente Glue".concat(ex.getMessage()));
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
