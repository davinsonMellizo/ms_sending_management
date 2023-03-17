package karate.utils.aws;

import karate.utils.aws.s3.S3Properties;
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
                .region(properties.get("region").toString())
                .endpoint(properties.get("endpoint").toString())
                .s3(S3Properties.fromMap((Map<String, Object>) properties.get("s3")))
                .build();
    }

    public boolean hasEndpoint() {
        return getEndpoint() != null && !getEndpoint().isEmpty();
    }

}
