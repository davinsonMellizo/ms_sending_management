package co.com.bancolombia.sqs.config;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.client.builder.ExecutorFactory;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
public class SQSConfig {
    private final SQSProperties properties;

    private ClientConfiguration clientConfiguration() {
        return new ClientConfiguration()
                .withConnectionTimeout(properties.getTimeout())
                .withRequestTimeout(properties.getTimeout())
                .withClientExecutionTimeout(properties.getTimeout());
    }

    private ExecutorFactory executorFactory() {
        return () -> new ThreadPoolExecutor(
                properties.getPoolSize(),
                properties.getPoolSize(),
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>());
    }

    @Bean
    @Primary
    @Profile({"dev", "qa", "pdn"})
    public AmazonSQSAsync clientSQS(){
        return AmazonSQSAsyncClientBuilder.standard()
                .withClientConfiguration(clientConfiguration())
                .withExecutorFactory(executorFactory())
                .withRegion(properties.getRegionAws())
                .build();
    }

    @Bean
    @Primary
    @Profile("local")
    public AmazonSQSAsync clientSQSLocal(){
        return AmazonSQSAsyncClientBuilder.standard()
                .withClientConfiguration(clientConfiguration())
                .withExecutorFactory(executorFactory())
                .withCredentials(new DefaultAWSCredentialsProviderChain())
                .withRegion(properties.getRegionAws())
                .build();
    }
}
