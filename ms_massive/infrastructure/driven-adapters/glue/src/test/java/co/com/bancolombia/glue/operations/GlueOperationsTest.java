package co.com.bancolombia.glue.operations;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.test.StepVerifier;
import software.amazon.awssdk.http.SdkHttpFullResponse;
import software.amazon.awssdk.http.SdkHttpResponse;
import software.amazon.awssdk.services.glue.GlueAsyncClient;
import software.amazon.awssdk.services.glue.model.*;

import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GlueOperationsTest {

    @Mock
    private GlueAsyncClient glueAsyncClient;

    @InjectMocks
    private GlueOperations glueOperations;

    private CompletableFuture<StartTriggerResponse> completableFutureTriggerStart;

    private static final String triggerName = "tgr_1_1_1";


    @BeforeAll
    void init() {
        MockitoAnnotations.openMocks(this);
        SdkHttpFullResponse httpFullResponse = SdkHttpResponse.builder()
                .statusCode(200)
                .build();

        StartTriggerResponse startTriggerResponse = (StartTriggerResponse) StartTriggerResponse
                .builder()
                .sdkHttpResponse(httpFullResponse)
                .build();

        completableFutureTriggerStart = CompletableFuture.completedFuture(startTriggerResponse);
    }

    @Test
    void startTriggerSuccess() {
        when(glueAsyncClient.startTrigger(any(StartTriggerRequest.class)))
                .thenReturn(completableFutureTriggerStart);

        glueOperations.startTrigger(triggerName)
                .as(StepVerifier::create)
                .expectNext(true)
                .verifyComplete();

        verify(glueAsyncClient, times(1))
                .startTrigger(any(StartTriggerRequest.class));
    }

}
