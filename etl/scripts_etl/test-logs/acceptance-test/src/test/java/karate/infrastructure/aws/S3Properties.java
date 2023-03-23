package karate.infrastructure.aws;

import lombok.Builder;
import lombok.Value;

import java.util.Map;

@Value
@Builder
public class S3Properties {

    String bucketNameData;
    String bucketPrefix;

    public static S3Properties fromMap(Map<String, Object> properties) {
        return S3Properties.builder()
                .bucketPrefix((String) properties.get("bucketPrefix"))
                .bucketNameData((String) properties.get("bucketNameData"))
                .build();
    }
}