package co.com.bancolombia.glue.operations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.glue.GlueAsyncClient;
import software.amazon.awssdk.services.glue.model.StartTriggerRequest;


@Component
@RequiredArgsConstructor
public class GlueOperations {
    private final GlueAsyncClient client;

    public Mono<Boolean> startTrigger(String name) {
        return Mono.fromFuture(client.startTrigger(
                StartTriggerRequest
                        .builder()
                        .name(name)
                        .build()
        )).map(response -> response.sdkHttpResponse().isSuccessful());
    }

}
