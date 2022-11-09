package co.com.bancolombia.glue.operations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.glue.GlueAsyncClient;
import software.amazon.awssdk.services.glue.model.*;


@Component
@RequiredArgsConstructor
public class GlueOperations {
    private final GlueAsyncClient client;


    public Mono<Boolean> createTrigger(String name, TriggerType type, String schedule,
                                       Action action, Boolean startOnCreation) {
        return Mono.fromFuture(client.createTrigger(
                CreateTriggerRequest
                        .builder()
                        .name(name)
                        .type(type)
                        .schedule(schedule)
                        .actions(action)
                        .startOnCreation(startOnCreation)
                        .build()
        )).map(response -> response.sdkHttpResponse().isSuccessful());
    }

    public Mono<Boolean> startTrigger(String name) {
        return Mono.fromFuture(client.startTrigger(
                StartTriggerRequest
                        .builder()
                        .name(name)
                        .build()
        )).map(response -> response.sdkHttpResponse().isSuccessful());
    }

    public Mono<Boolean> stopTrigger(String name) {
        return Mono.fromFuture(client.stopTrigger(
                StopTriggerRequest
                        .builder()
                        .name(name)
                        .build()
        )).map(response -> response.sdkHttpResponse().isSuccessful());
    }

    public Mono<Boolean> updateTrigger(String name, TriggerUpdate triggerUpdate) {
        return Mono.fromFuture(client.updateTrigger(
                UpdateTriggerRequest
                        .builder()
                        .name(name)
                        .triggerUpdate(triggerUpdate)
                        .build()
        )).map(response -> response.sdkHttpResponse().isSuccessful());
    }
}
