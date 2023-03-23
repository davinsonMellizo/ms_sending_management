package karate.utils.aws.s3;


import lombok.Builder;
import lombok.Value;

import java.util.Map;

@Value
@Builder
public class S3Properties {

    String bucketNameSource;
    String bucketNameTarget;
    String bucketProcessedMasiv;
    String massivePrefix;
    String massiveErrorPrefix;
    String emailPrefix;
    String smsPrefix;
    String pushPrefix;
    String emailMasivPrefix;
    String smsMasivPrefix;

    public static S3Properties fromMap(Map<String, Object> properties) {
        return S3Properties.builder()
                .bucketNameSource(properties.get("bucketNameSource").toString())
                .bucketNameTarget(properties.get("bucketNameTarget").toString())
                .bucketProcessedMasiv(properties.get("bucketProcessedMasiv").toString())
                .massivePrefix(properties.get("massivePrefix").toString())
                .massiveErrorPrefix(properties.get("massiveErrorPrefix").toString())
                .emailPrefix(properties.get("emailPrefix").toString())
                .smsPrefix(properties.get("smsPrefix").toString())
                .pushPrefix(properties.get("pushPrefix").toString())
                .emailMasivPrefix(properties.get("emailMasivPrefix").toString())
                .smsMasivPrefix(properties.get("smsMasivPrefix").toString())
                .build();
    }

}