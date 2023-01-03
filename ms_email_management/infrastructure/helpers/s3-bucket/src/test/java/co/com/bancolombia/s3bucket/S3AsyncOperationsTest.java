package co.com.bancolombia.s3bucket;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.internal.async.ByteArrayAsyncResponseTransformer;
import software.amazon.awssdk.http.SdkHttpMethod;
import software.amazon.awssdk.http.SdkHttpRequest;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.InputStream;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class S3AsyncOperationsTest {
    @InjectMocks
    private S3AsyncOperations s3AsyncOperations;
    @Mock
    private S3AsyncClient s3AsyncClient;
    @Mock
    private S3Presigner s3Presigner;

    private static final String bucketName = "bucket1";
    private static final String ccdtKey = "ccdtM1.json";
    private static final CompletableFuture<ResponseBytes> completableFuture
            = CompletableFuture.supplyAsync(() -> ResponseBytes.fromByteArray("test", "test".getBytes()));


    private static final CompletableFuture<InputStream> completableFutureInputStream
            =
            CompletableFuture.supplyAsync(() -> ResponseBytes.fromByteArray("test", "test".getBytes()).asInputStream());

    @Test
    void getFileAsStringTest() {
        when(s3AsyncClient.getObject(any(GetObjectRequest.class), any(ByteArrayAsyncResponseTransformer.class)))
                .thenReturn(completableFuture);
        StepVerifier.create(s3AsyncOperations.getFileAsString(bucketName, ccdtKey))
                .expectNext("test").verifyComplete();
    }

    @Test
    void getFileAsStringErrorTest() {
        when(s3AsyncClient.getObject(any(GetObjectRequest.class), any(ByteArrayAsyncResponseTransformer.class)))
                .thenReturn(Mono.error(new Throwable("testError")).toFuture());
        StepVerifier.create(s3AsyncOperations.getFileAsString(bucketName, ccdtKey))
                .expectError()
                .verify();
    }

    @Test
    void getFileAsInputStreamTest() {
        when(s3AsyncClient.getObject(any(GetObjectRequest.class), any(ByteArrayAsyncResponseTransformer.class)))
                .thenReturn(completableFuture);
        StepVerifier.create(s3AsyncOperations.getFileAsInputStream(bucketName, ccdtKey))
                .expectNextMatches(v -> Objects.nonNull(v))
                .verifyComplete();
    }

    @Test
    void getFileAsInputStreamErrorTest() {
        when(s3AsyncClient.getObject(any(GetObjectRequest.class), any(ByteArrayAsyncResponseTransformer.class)))
                .thenReturn(Mono.error(new Throwable("testError")).toFuture());
        StepVerifier.create(s3AsyncOperations.getFileAsInputStream(bucketName, ccdtKey))
                .expectError()
                .verify();
    }

    @Test
    void getFileAsByteTest() {
        when(s3AsyncClient.getObject(any(GetObjectRequest.class), any(ByteArrayAsyncResponseTransformer.class)))
                .thenReturn(completableFuture);
        StepVerifier.create(s3AsyncOperations.getFileAsByteArray(bucketName, ccdtKey))
                .expectNextMatches(v -> Objects.nonNull(v))
                .verifyComplete();
    }

    @Test
    void getFileAsByteErrorTest() {
        when(s3AsyncClient.getObject(any(GetObjectRequest.class), any(ByteArrayAsyncResponseTransformer.class)))
                .thenReturn(Mono.error(new Throwable("testError")).toFuture());
        StepVerifier.create(s3AsyncOperations.getFileAsByteArray(bucketName, ccdtKey))
                .expectError()
                .verify();
    }

    @Test
    void generatePresignedUrlTest() {
        ReflectionTestUtils.setField(s3AsyncOperations, "duration", 60L);
        PresignedGetObjectRequest presigned = PresignedGetObjectRequest.builder()
                .expiration(Instant.MIN)
                .isBrowserExecutable(true)
                .signedHeaders(Map.of("Authorization", List.of("Bearer 123456")))
                .httpRequest(SdkHttpRequest.builder().protocol("http").host("host").method(SdkHttpMethod.GET).build())
                .build();
        when(s3Presigner.presignGetObject(any(GetObjectPresignRequest.class))).thenReturn(presigned);
        StepVerifier.create(s3AsyncOperations.generatePresignedUrl(bucketName, ccdtKey))
                .assertNext(s -> assertThat(s).isEqualTo("http://host"))
                .verifyComplete();
    }

}