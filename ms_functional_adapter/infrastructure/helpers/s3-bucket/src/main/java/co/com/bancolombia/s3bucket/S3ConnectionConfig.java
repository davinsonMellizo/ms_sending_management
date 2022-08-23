package co.com.bancolombia.s3bucket;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProviderChain;
import software.amazon.awssdk.auth.credentials.ContainerCredentialsProvider;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.auth.credentials.InstanceProfileCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.auth.credentials.SystemPropertyCredentialsProvider;
import software.amazon.awssdk.auth.credentials.WebIdentityTokenFileCredentialsProvider;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3AsyncClientBuilder;
import software.amazon.awssdk.utils.ThreadFactoryBuilder;

import java.net.URI;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static software.amazon.awssdk.core.client.config.SdkAdvancedAsyncClientOption.FUTURE_COMPLETION_EXECUTOR;

@Configuration
@RequiredArgsConstructor
public class S3ConnectionConfig {

    private final S3ConnectionProperties s3ConnectionProperties;

    private ThreadPoolExecutor threadPoolExecutor() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                s3ConnectionProperties.getCorePoolSize(),
                s3ConnectionProperties.getMaximumPoolSize(),
                s3ConnectionProperties.getKeepAliveTime(),
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(s3ConnectionProperties.getQueueCapacity()),
                new ThreadFactoryBuilder().threadNamePrefix("sdk-async-response").build());
        executor.allowCoreThreadTimeOut(true);
        return executor;
    }

    public S3AsyncClient s3AsyncClient(boolean withEndpoint) {

        AwsCredentialsProviderChain chain = AwsCredentialsProviderChain.builder()
                .addCredentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .addCredentialsProvider(SystemPropertyCredentialsProvider.create())
                .addCredentialsProvider(WebIdentityTokenFileCredentialsProvider.create())
                .addCredentialsProvider(ProfileCredentialsProvider.create())
                .addCredentialsProvider(ContainerCredentialsProvider.builder().build())
                .addCredentialsProvider(InstanceProfileCredentialsProvider.create())
                .build();

        S3AsyncClientBuilder s3AsyncClient = S3AsyncClient.builder()
                .credentialsProvider(chain)
                .asyncConfiguration(config -> config.advancedOption(FUTURE_COMPLETION_EXECUTOR, threadPoolExecutor()))
                .region(s3ConnectionProperties.getRegion());
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
