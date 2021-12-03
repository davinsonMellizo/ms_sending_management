package co.com.bancolombia.ses.adapter;

import co.com.bancolombia.model.message.TemplateEmail;
import co.com.bancolombia.model.message.gateways.PinpointGateway;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.pinpoint.PinpointAsyncClient;
import software.amazon.awssdk.services.pinpoint.model.GetEmailTemplateRequest;

@Component
@RequiredArgsConstructor
public class PinpointAdapter implements PinpointGateway {
    private final PinpointAsyncClient client;

    @Override
    public Mono<TemplateEmail> findTemplateEmail(String templateName){
        return Mono.fromFuture(client.getEmailTemplate(GetEmailTemplateRequest.builder()
                .templateName(templateName)
                .build()))
                .map(response -> TemplateEmail.builder()
                        .subject(response.emailTemplateResponse().subject())
                        .bodyHtml(response.emailTemplateResponse().htmlPart())
                        .textPlain(response.emailTemplateResponse().textPart())
                .build());
    }
}
