package co.com.bancolombia.glueclient.config;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.glue.AWSGlue;
import com.amazonaws.services.glue.AWSGlueClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
@RequiredArgsConstructor
public class GlueClientConfig {

    private final AwsGlueProperties awsProperties;

    @Bean
    public AWSGlue getGlueClient() {
        var builder = AWSGlueClient.builder();
        if(Objects.nonNull(awsProperties.getEndpoint())){
            var configuration =
                    new AwsClientBuilder.EndpointConfiguration(awsProperties.getEndpoint(), awsProperties.getRegion());
            return builder.withEndpointConfiguration(configuration).build();
        }else{
            return builder.withRegion(awsProperties.getRegion()).build();
        }
    }
}
