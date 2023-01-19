package co.com.bancolombia.ses.adapter;

import co.com.bancolombia.model.message.Alert;
import co.com.bancolombia.model.message.Attachment;
import co.com.bancolombia.model.message.TemplateEmail;
import co.com.bancolombia.s3bucket.S3AsyncOperations;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import software.amazon.awssdk.services.sesv2.SesV2AsyncClient;
import software.amazon.awssdk.services.sesv2.model.SendEmailRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SesAdapterTest {

    @InjectMocks
    private SesAdapter sesAdapter;
    @Mock
    private SesV2AsyncClient client;
    @Mock
    private S3AsyncOperations s3AsyncOperations;

    @Test
    void findTemplateEmailTest() {
        TemplateEmail templateEmail = TemplateEmail.builder()
                .bodyHtml("html").subject("subject").build();
        Alert alert = Alert.builder()
                .from("from").destination(Alert.Destination.builder().toAddress("address").build())
                .build();
        when(client.sendEmail((SendEmailRequest) any()))
                .thenReturn(new CompletableFuture<>());
        StepVerifier.create(sesAdapter.sendEmail(templateEmail, alert))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void pathTest() {
        ReflectionTestUtils.setField(sesAdapter, "attachmentBucket", "attachmentBucket");
        TemplateEmail templateEmail = TemplateEmail.builder().bodyHtml("html").subject("subject").build();
        List<Attachment> attachmentList = new ArrayList<>();
        attachmentList.add(Attachment.builder().type("Path").value("path/to/file/test.pdf").filename("test.pdf").build());
        Alert alert = Alert.builder()
                .from("from")
                .destination(Alert.Destination.builder().toAddress("address").build())
                .attachments(attachmentList)
                .build();
        byte[] response = new byte[10];
        when(s3AsyncOperations.getFileAsByteArray(anyString(), anyString())).thenReturn(Mono.just(response));
        when(client.sendEmail((SendEmailRequest) any()))
                .thenReturn(new CompletableFuture<>());
        StepVerifier.create(sesAdapter.sendEmail(templateEmail, alert))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void urlTest() {
        TemplateEmail templateEmail = TemplateEmail.builder().bodyHtml("html").subject("subject").build();
        List<Attachment> attachmentList = new ArrayList<>();
        attachmentList.add(Attachment.builder().type("Url").value("http://url/test.pdf").filename("test.pdf").build());
        Alert alert = Alert.builder()
                .from("from")
                .destination(Alert.Destination.builder().toAddress("address").build())
                .attachments(attachmentList)
                .build();
        StepVerifier.create(sesAdapter.sendEmail(templateEmail, alert))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void base64Test() {
        TemplateEmail templateEmail = TemplateEmail.builder().bodyHtml("html").subject("subject").build();
        List<Attachment> attachmentList = new ArrayList<>();
        attachmentList.add(Attachment.builder()
                .type("Base64").contentType("application/pdf").value("Base64Text").filename("test.pdf").build());
        Alert alert = Alert.builder()
                .from("from")
                .destination(Alert.Destination.builder().toAddress("address").build())
                .attachments(attachmentList)
                .build();
        when(client.sendEmail((SendEmailRequest) any()))
                .thenReturn(new CompletableFuture<>());
        StepVerifier.create(sesAdapter.sendEmail(templateEmail, alert))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void defaultTest() {
        TemplateEmail templateEmail = TemplateEmail.builder().bodyHtml("html").subject("subject").build();
        List<Attachment> attachmentList = new ArrayList<>();
        attachmentList.add(Attachment.builder().type("Typo").build());
        Alert alert = Alert.builder()
                .from("from")
                .destination(Alert.Destination.builder().toAddress("address").build())
                .attachments(attachmentList)
                .build();
        StepVerifier.create(sesAdapter.sendEmail(templateEmail, alert))
                .expectNextCount(1)
                .verifyComplete();
    }
}
