package co.com.bancolombia.ses.config;

import co.com.bancolombia.model.log.LoggerBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sesv2.SesV2AsyncClient;

import java.net.URI;


@RequiredArgsConstructor
@Configuration
public class SesConfig {

    private static final Region region = Region.US_EAST_1;
    private final LoggerBuilder logger;
    private final PropertiesSES propertiesSES;

    @Bean
    @Profile({"dev", "qa", "pdn"})
    public SesV2AsyncClient sesClientConfig() {
        try {
            return SesV2AsyncClient.builder()
                    .region(region)
                    .build();

        } catch (IllegalStateException | ExceptionInInitializerError ex) {
            logger.info("exception " + ex);
            logger.info("Error al inicializar cliente ses".concat(ex.getMessage()));
        }
        return null;
    }

    @Bean
    @Profile({"local"})
    public SesV2AsyncClient clientLocalSes(){
        return SesV2AsyncClient.builder()
                .region(region)
                .endpointOverride(URI.create(propertiesSES.getEndpoint()))
                .build();
    }
}
