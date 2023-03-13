package karate.utils.aws.s3;

import karate.utils.aws.AwsProperties;
import lombok.Getter;
import software.amazon.awssdk.endpoints.Endpoint;
import software.amazon.awssdk.services.s3.S3ClientBuilder;

import java.net.URI;
import java.util.concurrent.CompletableFuture;

@Getter
public class S3Client {

    private final software.amazon.awssdk.services.s3.S3Client awsS3Client;

    private final S3Properties properties;

    public S3Client(AwsProperties awsProperties) {
        S3ClientBuilder s3ClientBuilder = software.amazon.awssdk.services.s3.S3Client.builder();
        if (awsProperties.hasEndpoint()) {
            s3ClientBuilder = s3ClientBuilder.endpointProvider(endpointParams ->
                    CompletableFuture.completedFuture(Endpoint
                            .builder()
                            .url(URI.create(
                                    String.join("/", awsProperties.getEndpoint(), endpointParams.bucket())
                            ))
                            .build()
                    )
            );
        }
        awsS3Client = s3ClientBuilder.build();
        properties = awsProperties.getS3();
    }
}
