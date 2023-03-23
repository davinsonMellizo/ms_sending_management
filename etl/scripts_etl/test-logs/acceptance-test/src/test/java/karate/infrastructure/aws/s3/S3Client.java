package karate.infrastructure.aws.s3;

import karate.infrastructure.aws.AwsProperties;
import karate.infrastructure.aws.S3Properties;
import software.amazon.awssdk.endpoints.Endpoint;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.model.Bucket;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.net.URI;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.stream.Collectors.toList;

public class S3Client {

    private final software.amazon.awssdk.services.s3.S3Client awsS3Client;
    private final S3Properties s3Properties;

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
        s3Properties = awsProperties.getS3();
    }

    public List<String> getListBuckets() {
        return awsS3Client
                .listBuckets()
                .buckets()
                .stream()
                .map(Bucket::name)
                .collect(toList());
    }

    public List<S3Object> getObject() {
        String prefix = s3Properties.getBucketPrefix().concat("/")
                .concat(LocalDate.now().minus(31, ChronoUnit.DAYS).toString()) .concat("/");
        return awsS3Client
                .listObjects(ListObjectsRequest.builder()
                        .bucket(s3Properties.getBucketNameData())
                        .prefix(prefix)
                        .build())
                .contents()
                .stream()
                .filter(x -> x.lastModified()
                        .isAfter(Instant.now().minus(20, ChronoUnit.MINUTES)))
                .collect(toList());
    }
}
