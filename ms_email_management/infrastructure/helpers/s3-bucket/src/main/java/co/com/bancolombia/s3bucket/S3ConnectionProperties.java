package co.com.bancolombia.s3bucket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
    public static final int CORE_POOL_SIZE = 50;
    public static final int MAXIMUM_POOL_SIZE = 50;
    public static final int KEEP_ALIVE_TIME = 10;
    public static final int QUEUE_CAPACITY = 10_000;

    @Builder.Default
    private Region region = Region.US_EAST_1;

    @Builder.Default
    private String endpoint = "http://localhost:4566";

    @Builder.Default
    int corePoolSize = CORE_POOL_SIZE;

    @Builder.Default
    int maximumPoolSize = MAXIMUM_POOL_SIZE;

    @Builder.Default
    int keepAliveTime = KEEP_ALIVE_TIME;

    @Builder.Default
    int queueCapacity = QUEUE_CAPACITY;

}