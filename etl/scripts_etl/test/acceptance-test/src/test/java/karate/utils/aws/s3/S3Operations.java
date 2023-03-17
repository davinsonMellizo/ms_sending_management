package karate.utils.aws.s3;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.io.FileOutputStream;
import java.util.Optional;

@RequiredArgsConstructor
public class S3Operations {

    private final S3Client s3Client;

    @SneakyThrows
    private byte[] readAllBytes(ResponseInputStream<GetObjectResponse> objRequest) {
        return IOUtils.toByteArray(objRequest);
    }

    @SneakyThrows
    public String saveFile(String key, String bucketName) {
        return Optional.ofNullable(GetObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .build())
                .map(s3Client.getAwsS3Client()::getObject)
                .map(this::readAllBytes)
                .map(bytes -> {
                    String fileName = "massive-file";
                    try (FileOutputStream fos = new FileOutputStream(fileName)) {
                        fos.write(bytes);
                    } catch (Exception ignored) {
                    }
                    return fileName;
                }).orElse(null);
    }

    private ListObjectsV2Request getListObjectsV2Request(String bucketName, String prefix) {
        return ListObjectsV2Request
                .builder()
                .bucket(bucketName)
                .prefix(prefix)
                .build();
    }

    @SneakyThrows
    public Optional<S3Object> getRecordsFromBucket(String bucketName, String prefix) {
        ListObjectsV2Request listObjects = getListObjectsV2Request(bucketName, prefix);
        return s3Client.getAwsS3Client().listObjectsV2(listObjects).contents().stream()
                .filter(obj -> obj.size() > 0)
                .reduce((first, second) -> second);
    }


}
