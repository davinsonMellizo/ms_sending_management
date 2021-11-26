package co.com.bancolombia.pinpoint.adapter;

import co.com.bancolombia.model.message.TemplateSms;
import co.com.bancolombia.model.message.gateways.PinpointGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.pinpoint.PinpointAsyncClient;
import software.amazon.awssdk.services.pinpoint.model.GetSmsTemplateRequest;

@Repository
@RequiredArgsConstructor
public class PinpointAdapter implements PinpointGateway {
    private final PinpointAsyncClient client;

    @Override
    public Mono<TemplateSms> findTemplateSms(String templateName){
        return Mono.fromFuture(client.getSmsTemplate(GetSmsTemplateRequest.builder()
                .templateName(templateName)
                .build())).map(response -> TemplateSms.builder().build());
    }
}
