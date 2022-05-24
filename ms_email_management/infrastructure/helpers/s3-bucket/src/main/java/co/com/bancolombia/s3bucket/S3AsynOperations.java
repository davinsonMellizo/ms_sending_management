package co.com.bancolombia.s3bucket;

import co.com.bancolombia.commons.exceptions.TechnicalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.BytesWrapper;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.io.InputStream;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.TECHNICAL_S3_EXCEPTION;
import static software.amazon.awssdk.core.ResponseBytes.fromByteArray;

@Component
@RequiredArgsConstructor
public class S3AsynOperations {

    private final S3AsyncClient s3AsyncClient;

    public Mono<String> getFileAsString(String bucketName, String objectKey){
        return getRequest(bucketName, objectKey)
                .flatMap(this::getFileAsBytes)
                .map(response -> fromByteArray(response, response.asByteArray()))
                .map(BytesWrapper::asUtf8String)
                .onErrorMap(error -> {
                    String message = String.join(" ", bucketName, objectKey, error.getMessage());
                    return new TechnicalException(message, TECHNICAL_S3_EXCEPTION);
                });
    }

    private Mono<ResponseBytes> getFileAsBytes(GetObjectRequest request){
        return Mono.fromFuture(s3AsyncClient.getObject(request, AsyncResponseTransformer.toBytes()));
    }

    public Mono<InputStream> getFileAsInputStream(String bucketName, String objectKey){
        return getRequest(bucketName, objectKey)
                .flatMap(this::getFileAsBytes)
                .map(response -> fromByteArray(response, response.asByteArray()))
                .map(BytesWrapper::asInputStream)
                .onErrorMap(error -> {
                    String message = String.join(" ", bucketName, objectKey, error.getMessage());
                    return new TechnicalException(message, TECHNICAL_S3_EXCEPTION);
                });
    }

    private Mono<GetObjectRequest> getRequest(String bucketName, String objectKey){
        return Mono.just(GetObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build());
    }
}