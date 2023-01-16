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
import software.amazon.awssdk.services.glue.model.Action;
import software.amazon.awssdk.services.glue.model.CreateTriggerRequest;
import software.amazon.awssdk.services.glue.model.CreateTriggerResponse;
import software.amazon.awssdk.services.glue.model.DeleteTriggerRequest;
import software.amazon.awssdk.services.glue.model.DeleteTriggerResponse;
import software.amazon.awssdk.services.glue.model.StartTriggerRequest;
import software.amazon.awssdk.services.glue.model.StartTriggerResponse;
import software.amazon.awssdk.services.glue.model.StopTriggerRequest;
import software.amazon.awssdk.services.glue.model.StopTriggerResponse;
import software.amazon.awssdk.services.glue.model.TriggerType;
import software.amazon.awssdk.services.glue.model.TriggerUpdate;
import software.amazon.awssdk.services.glue.model.UpdateTriggerRequest;
import software.amazon.awssdk.services.glue.model.UpdateTriggerResponse;

import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GlueOperationsTest {

    @Mock
    private GlueAsyncClient glueAsyncClient;

    @InjectMocks
    private GlueOperations glueOperations;

    private CompletableFuture<CreateTriggerResponse> completableFutureTriggerCreate;
    private CompletableFuture<StartTriggerResponse> completableFutureTriggerStart;
    private CompletableFuture<StopTriggerResponse> completableFutureTriggerStop;
    private CompletableFuture<UpdateTriggerResponse> completableFutureTriggerUpdate;
    private CompletableFuture<DeleteTriggerResponse> completableFutureTriggerDelete;
    private static final String triggerName = "tgr_1_1_1";


    @BeforeAll
    void init() {
        MockitoAnnotations.openMocks(this);
        SdkHttpFullResponse httpFullResponse = SdkHttpResponse.builder()
                .statusCode(200)
                .build();

        CreateTriggerResponse createTriggerResponse = (CreateTriggerResponse) CreateTriggerResponse
                .builder()
                .sdkHttpResponse(httpFullResponse)
                .build();

        StartTriggerResponse startTriggerResponse = (StartTriggerResponse) StartTriggerResponse
                .builder()
                .sdkHttpResponse(httpFullResponse)
                .build();

        StopTriggerResponse stopTriggerResponse = (StopTriggerResponse) StopTriggerResponse
                .builder()
                .sdkHttpResponse(httpFullResponse)
                .build();

        UpdateTriggerResponse updateTriggerResponse = (UpdateTriggerResponse) UpdateTriggerResponse
                .builder()
                .sdkHttpResponse(httpFullResponse)
                .build();

        DeleteTriggerResponse deleteTriggerResponse = (DeleteTriggerResponse) DeleteTriggerResponse
                .builder()
                .sdkHttpResponse(httpFullResponse)
                .build();

        completableFutureTriggerCreate = CompletableFuture.completedFuture(createTriggerResponse);
        completableFutureTriggerStart = CompletableFuture.completedFuture(startTriggerResponse);
        completableFutureTriggerStop = CompletableFuture.completedFuture(stopTriggerResponse);
        completableFutureTriggerUpdate = CompletableFuture.completedFuture(updateTriggerResponse);
        completableFutureTriggerDelete = CompletableFuture.completedFuture(deleteTriggerResponse);
    }

    @Test
    void createTriggerSuccess() {
        when(glueAsyncClient.createTrigger(any(CreateTriggerRequest.class)))
                .thenReturn(completableFutureTriggerCreate);

        glueOperations.createTrigger(triggerName, TriggerType.SCHEDULED,
                        "cron()", Action.builder().build(), true)
                .as(StepVerifier::create)
                .expectNext(true)
                .verifyComplete();

        verify(glueAsyncClient, times(1))
                .createTrigger(any(CreateTriggerRequest.class));
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

    @Test
    void stopTriggerSuccess() {
        when(glueAsyncClient.stopTrigger(any(StopTriggerRequest.class)))
                .thenReturn(completableFutureTriggerStop);

        glueOperations.stopTrigger(triggerName)
                .as(StepVerifier::create)
                .expectNext(true)
                .verifyComplete();

        verify(glueAsyncClient, times(1))
                .stopTrigger(any(StopTriggerRequest.class));
    }

    @Test
    void updateTriggerSuccess() {
        when(glueAsyncClient.updateTrigger(any(UpdateTriggerRequest.class)))
                .thenReturn(completableFutureTriggerUpdate);

        glueOperations.updateTrigger(triggerName, TriggerUpdate.builder().build())
                .as(StepVerifier::create)
                .expectNext(true)
                .verifyComplete();

        verify(glueAsyncClient, times(1))
                .updateTrigger(any(UpdateTriggerRequest.class));
    }

    @Test
    void deleteTrigger() {
        when(glueAsyncClient.deleteTrigger(any(DeleteTriggerRequest.class)))
                .thenReturn(completableFutureTriggerDelete);

        glueOperations.deleteTrigger(triggerName)
                .as(StepVerifier::create)
                .expectNext(true)
                .verifyComplete();

        verify(glueAsyncClient, times(1))
                .deleteTrigger(any(DeleteTriggerRequest.class));
    }

}
