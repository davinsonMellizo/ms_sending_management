package co.com.bancolombia.s3bucket;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.internal.async.ByteArrayAsyncResponseTransformer;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.io.InputStream;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class S3AsyncOperationsTest {
    @InjectMocks
    private S3AsyncOperations s3AsyncOperations;
    @Mock
    private S3AsyncClient s3AsyncClient;
    private static final String bucketName = "bucket1";
    private static final String ccdtKey = "ccdtM1.json";
    private static final CompletableFuture<ResponseBytes> completableFuture
            = CompletableFuture.supplyAsync(() -> ResponseBytes.fromByteArray("test", "test".getBytes()));

    private static final CompletableFuture<InputStream> completableFutureInputStream
            = CompletableFuture.supplyAsync(() -> ResponseBytes.fromByteArray("test", "test".getBytes()).asInputStream());

    @Test
    void getFileAsStringTest() {
        when(s3AsyncClient.getObject(any(GetObjectRequest.class), any(ByteArrayAsyncResponseTransformer.class)))
                .thenReturn(completableFuture);
        StepVerifier.create(s3AsyncOperations.getFileAsString(bucketName, ccdtKey))
                .expectNext("test").verifyComplete();
    }

    @Test
    void getFileAsStringErrorTest(){
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
    void getFileAsInputStreamErrorTest(){
        when(s3AsyncClient.getObject(any(GetObjectRequest.class), any(ByteArrayAsyncResponseTransformer.class)))
                .thenReturn(Mono.error(new Throwable("testError")).toFuture());
        StepVerifier.create(s3AsyncOperations.getFileAsInputStream(bucketName, ccdtKey))
                .expectError()
                .verify();
    }

}