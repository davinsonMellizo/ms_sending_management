package co.com.bancolombia.sqs.config;

import co.com.bancolombia.commons.enums.TechnicalExceptionEnum;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import lombok.Generated;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

import java.net.URI;

@Configuration
@RequiredArgsConstructor
@Generated
public class SQSConfig {

    private final SQSProperties properties;

    @Bean
    public SqsAsyncClient sqsConfig() {
        try {
            return SqsAsyncClient.builder()
                    .region(Region.US_EAST_1)
                    .endpointOverride(URI.create(properties.getUrl()))
                    .build();
        } catch (TechnicalException e) {
            throw new TechnicalException(TechnicalExceptionEnum.SEND_LOG_SQS_ERROR);
        }
    }
}
