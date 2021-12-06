package co.com.bancolombia.pinpoint.adapter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;
import software.amazon.awssdk.services.pinpoint.PinpointAsyncClient;
import software.amazon.awssdk.services.pinpoint.model.*;

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
        when(client.getSmsTemplate((GetSmsTemplateRequest) any()))
                .thenReturn(CompletableFuture.completedFuture(GetSmsTemplateResponse.builder()
                        .smsTemplateResponse(SMSTemplateResponse.builder()
                                .body("text")
                                .build())
                        .build()));
        StepVerifier.create(pinpointAdapter.findTemplateSms("compra"))
                .consumeNextWith(templateSms -> templateSms.getBodyText().equals("text"))
                .verifyComplete();
    }
}
