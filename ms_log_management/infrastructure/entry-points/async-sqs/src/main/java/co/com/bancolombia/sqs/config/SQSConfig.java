package co.com.bancolombia.sqs.config;



import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.MustacheFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.*;
import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@Configuration
@Profile({"dev", "qa", "pdn"})
@RequiredArgsConstructor
public class SQSConfig {

    @Bean
    public MustacheFactory mustacheFactory(){
        return new DefaultMustacheFactory();
    }

    @Bean
    public SqsAsyncClient sqsAsyncClient() {
        AwsCredentialsProviderChain chain = AwsCredentialsProviderChain.builder()
                .addCredentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .addCredentialsProvider(SystemPropertyCredentialsProvider.create())
                .addCredentialsProvider(WebIdentityTokenFileCredentialsProvider.create())
                .addCredentialsProvider(ProfileCredentialsProvider.create())
                .addCredentialsProvider(ContainerCredentialsProvider.builder().build())
                .addCredentialsProvider(InstanceProfileCredentialsProvider.create())
                .build();

        return SqsAsyncClient.builder()
                .credentialsProvider(chain)
                .region(Region.US_EAST_1)
                .build();
    }
}

