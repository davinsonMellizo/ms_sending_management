package karate.infrastructure.aws;


import lombok.Builder;
import lombok.Value;

import java.util.Map;

@Value
@Builder
public class S3Properties {

    String bucketNameSource;
    String bucketNameTarget;
    String massivePrefix;
    String emailPrefix;
    String smsPrefix;
    String pushPrefix;

    public static S3Properties fromMap(Map<String, Object> properties) {
        return S3Properties.builder()
                .bucketNameSource((String) properties.get("bucketNameSource"))
                .bucketNameTarget((String) properties.get("bucketNameTarget"))
                .massivePrefix((String) properties.get("massivePrefix"))
                .emailPrefix((String) properties.get("emailPrefix"))
                .smsPrefix((String) properties.get("smsPrefix"))
                .pushPrefix((String) properties.get("pushPrefix"))
                .build();
    }

}