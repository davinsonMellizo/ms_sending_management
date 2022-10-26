package co.com.bancolombia.ses.adapter;

import co.com.bancolombia.model.message.Alert;
import co.com.bancolombia.model.message.TemplateEmail;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;
import software.amazon.awssdk.services.ses.SesAsyncClient;
import software.amazon.awssdk.services.ses.model.SendRawEmailRequest;

import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SesAdapterTest {

    @InjectMocks
    private SesAdapter sesAdapter;
    @Mock
    private SesAsyncClient client;

    @Test
    public void findTemplateEmailTest() {
        /*
        TemplateEmail templateEmail = TemplateEmail.builder()
                .bodyHtml("html").subject("subject").build();
        Alert alert = Alert.builder()
                .from("from").destination(Alert.Destination.builder().toAddress("address").build())
                .build();
        when(client.sendRawEmail((SendRawEmailRequest) any()))
                .thenReturn(new CompletableFuture<>());
        StepVerifier.create(sesAdapter.sendEmail(templateEmail, alert))
                .expectNextCount(1)
                .verifyComplete();*/
    }
}
