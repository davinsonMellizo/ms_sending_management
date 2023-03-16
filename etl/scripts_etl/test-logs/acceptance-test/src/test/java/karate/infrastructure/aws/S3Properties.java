package karate.infrastructure.aws;

import lombok.Builder;
import lombok.Value;

import java.util.Map;

@Value
@Builder
public class S3Properties {

    String bucketNameData;

    public static S3Properties fromMap(Map<String, Object> properties) {
        return S3Properties.builder()
                .bucketNameData((String) properties.get("bucketNameData"))
                .build();
    }
}