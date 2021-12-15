package co.com.bancolombia.dynamodb.adapter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;

import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class DynamoAdapterTest {
    /*@InjectMocks
    private DynamoAdapter dynamoAdapter;
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
        StepVerifier.create(dynamoAdapter.findTemplateEmail("compra"))
                .consumeNextWith(templateEmail -> templateEmail.getBodyHtml().equals("html"))
                .verifyComplete();
    }*/
}
