package co.com.bancolombia.dynamodb.config;

import co.com.bancolombia.commons.enums.TechnicalExceptionEnum;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;

import java.net.URI;


@Configuration
@RequiredArgsConstructor
public class DynamoConfig {

    private final Properties properties;

    @Bean
    public DynamoDbEnhancedAsyncClient dynamodbConfig() {
        try {
            DynamoDbAsyncClient ddb = DynamoDbAsyncClient.builder()
                    .region(Region.US_EAST_1)
                    .build();
            return DynamoDbEnhancedAsyncClient.builder()
                    .dynamoDbClient(ddb)
                    .build();
        } catch (TechnicalException e) {
            throw new TechnicalException(TechnicalExceptionEnum.SEND_LOG_SQS_ERROR);
        }
    }

    /*@Bean
    @Profile({"dev", "cer", "pdn"})
    public DynamoDbAsyncClient clientEnvironments(){
        return DynamoDbAsyncClient.create();
    }

    @Bean
    @Profile({"local"})
    public DynamoDbAsyncClient clientLocal(){
        return DynamoDbAsyncClient.builder()
                .region(Region.of(properties.getRegionAws()))
                .endpointOverride(URI.create(properties.getEndpoint()))
                .build();
    }

    @Bean
    @Primary
    public DynamoDbEnhancedAsyncClient clientEnhanced(final DynamoDbAsyncClient client){
        return DynamoDbEnhancedAsyncClient.builder()
                .dynamoDbClient(client)
                .build();
    }*/

}
