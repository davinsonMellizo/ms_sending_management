package co.com.bancolombia.pinpoint.adapter;

import co.com.bancolombia.ses.adapter.PinpointAdapter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;
import software.amazon.awssdk.services.pinpoint.PinpointAsyncClient;
import software.amazon.awssdk.services.pinpoint.model.EmailTemplateResponse;
import software.amazon.awssdk.services.pinpoint.model.GetEmailTemplateRequest;
import software.amazon.awssdk.services.pinpoint.model.GetEmailTemplateResponse;

import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class PinpointAdapterTest {
    @InjectMocks
    private PinpointAdapter pinpointAdapter;
    @Mock
    private PinpointAsyncClient client;

    @Test
    public void findTemplateEmailTest(){
        when(client.getEmailTemplate((GetEmailTemplateRequest)any()))
                .thenReturn(CompletableFuture.completedFuture(GetEmailTemplateResponse.builder()
                        .emailTemplateResponse(EmailTemplateResponse.builder()
                                .subject("subject")
                                .htmlPart("html")
                                .textPart("text")
                                .build())
                        .build()));
        StepVerifier.create(pinpointAdapter.findTemplateEmail("compra"))
                .consumeNextWith(templateEmail -> templateEmail.getBodyHtml().equals("html"))
                .verifyComplete();
    }
}
