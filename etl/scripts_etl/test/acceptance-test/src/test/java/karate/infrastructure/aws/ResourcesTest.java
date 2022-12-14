package karate.infrastructure.aws;

import karate.infrastructure.aws.s3.S3Client;

import java.util.HashMap;
import java.util.Map;

public class ResourcesTest {
    public final S3Client s3Client;

    public ResourcesTest() {
        Map<String, Object> properties = new HashMap<>();
        Map<String, Object> s3Properties = new HashMap<>();

        s3Properties.put("bucketNameSource", "nu0154001-alertas-dev-glue-scripts-data");
        s3Properties.put("bucketNameTarget", "nu0154001-alertas-dev-glue-processed-data");
        s3Properties.put("massivePrefix", "test");
        s3Properties.put("emailPrefix", "test/ChannelType=EMAIL");
        s3Properties.put("smsPrefix", "test/ChannelType=SMS");
        s3Properties.put("pushPrefix", "test/ChannelType=PUSH");

        properties.put("region", "us-east-1");
        properties.put("endpoint", "http://localhost:4566");
        properties.put("s3", s3Properties);
        s3Client = new S3Client(AwsProperties.fromMap(properties));
    }

    public static void main(String... s) {
        ResourcesTest test = new ResourcesTest();
        test.s3Client.validateContactsFlow();
    }
}
