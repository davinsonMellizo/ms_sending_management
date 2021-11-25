package co.com.bancolombia.pinpoint.config;

import co.com.bancolombia.commons.enums.TechnicalExceptionEnum;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.pinpoint.PinpointAsyncClient;


@Configuration
public class PinpointConfig {

    @Profile("local")
    @Bean
    public PinpointAsyncClient pinpointConfigLocal(){
        try {
            return PinpointAsyncClient.builder()
                    .region(Region.US_EAST_1)
                    .credentialsProvider(getProviderChain())
                    .build();
        }catch (TechnicalException e){
            throw new TechnicalException(TechnicalExceptionEnum.CREATE_CLIENT_PINPOINT_ERROR);
        }
    }

    @Profile({"dev", "qa", "pdn"})
    @Bean
    public PinpointAsyncClient pinpointConfig(){
        try {
            return PinpointAsyncClient.builder()
                    .region(Region.US_EAST_1)
                    .build();
        }catch (TechnicalException e){
            throw new TechnicalException(TechnicalExceptionEnum.CREATE_CLIENT_PINPOINT_ERROR);
        }
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
