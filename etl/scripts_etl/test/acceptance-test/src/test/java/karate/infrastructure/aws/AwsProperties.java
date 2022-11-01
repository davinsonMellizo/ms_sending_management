package karate.infrastructure.aws;

import lombok.Builder;
import lombok.Value;

import java.util.Map;

@Value
@Builder
public class AwsProperties {

    String region;
    String endpoint;
    S3Properties s3;

    @SuppressWarnings("unchecked")
    public static AwsProperties fromMap(Map<String, Object> properties) {
        return AwsProperties.builder()
                .region((String) properties.get("region"))
                .endpoint((String) properties.get("endpoint"))
                .s3(S3Properties.fromMap((Map<String, Object>) properties.get("s3")))
                .build();
    }

    public boolean hasEndpoint() {
        return getEndpoint() != null && !getEndpoint().isEmpty();
    }

    public boolean hasRegion() {
        return getRegion() != null && !getRegion().isEmpty();
    }
}
