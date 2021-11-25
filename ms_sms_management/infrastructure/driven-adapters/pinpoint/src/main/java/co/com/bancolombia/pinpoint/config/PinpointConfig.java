package co.com.bancolombia.pinpoint.config;

import co.com.bancolombia.commons.enums.TechnicalExceptionEnum;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.pinpoint.PinpointAsyncClient;

import java.net.URI;

@Configuration
@RequiredArgsConstructor
public class PinpointConfig {

    private final PinpointProperties properties;

    @Bean
    @Primary
    public PinpointAsyncClient clientPinpoint(){
        try {
            return PinpointAsyncClient.builder()
                    .region(Region.US_EAST_1)
                    .endpointOverride(URI.create(properties.getUrl()))
                    .build();
        }catch (TechnicalException e){
            throw new TechnicalException(TechnicalExceptionEnum.CREATE_CLIENT_PINPOINT_ERROR);
        }
    }
}
