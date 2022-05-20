package co.com.bancolombia.s3bucket;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.regions.Region;

@Getter
@Setter
@Builder
@Component
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "adapters.s3.commons")
public class S3ConnectionProperties {

    @Builder.Default
    private Region region = Region.US_EAST_1;

    @Builder.Default
    private String endpoint = "http://localhost:4566";

    @Builder.Default
    int corePoolSize = 50;

    @Builder.Default
    int maximumPoolSize = 50;

    @Builder.Default
    int keepAliveTime = 10;

    @Builder.Default
    int queueCapacity = 10_000;

}