package co.com.bancolombia.usecase.sendalert;

import co.com.bancolombia.binstash.api.ObjectCache;
import co.com.bancolombia.model.message.Alert;
import co.com.bancolombia.model.message.Attachment;
import co.com.bancolombia.model.message.Mail;
import co.com.bancolombia.model.message.Parameter;
import co.com.bancolombia.model.message.Response;
import co.com.bancolombia.model.message.Template;
import co.com.bancolombia.model.message.TemplateEmail;
import co.com.bancolombia.model.message.gateways.MasivianGateway;
import co.com.bancolombia.model.message.gateways.SesGateway;
import co.com.bancolombia.model.message.gateways.TemplateEmailGateway;
import co.com.bancolombia.model.message.gateways.TemplateGateway;
import co.com.bancolombia.model.token.DynamoGateway;
import co.com.bancolombia.model.token.SecretGateway;
import co.com.bancolombia.usecase.log.LogUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SendAlertUseCaseTest {

    @InjectMocks
    private SendAlertUseCase useCase;
    @Mock
    private TemplateGateway templateEmailGateway;
    @Mock
    private LogUseCase logUseCase;
    @Mock
    private MasivianGateway masivianGateway;
    @Mock
    private SesGateway sesGateway;
    @Mock
    ObjectCache<ArrayList> objectCache;
    @Mock
    DynamoGateway dynamoGateway;
    @Mock
    SecretGateway secretGateway;
    @Mock
    private GeneratorTokenUseCase generatorTokenUseCase;
    private Alert alert = new Alert();

    @BeforeEach
    public void init() {
        alert.setProvider("MAS");
        alert.setFrom("bancolombia@test.com.co");
        alert.setDestination(new Alert.Destination("bancolombia@test.com.co", "", ""));
        alert.setAttachments(new ArrayList<>());
        Map <String, Object> parameters = new HashMap<>();
        parameters.put("name", "bancolombia");
        alert.setTemplate(new Template( parameters, "Compra"));
        alert.setLogKey(UUID.randomUUID().toString());
    }

    @Test
    void sendAlertMasivianErrorTest() {
        TemplateEmail template =
                new TemplateEmail("subject", "<div>Hola ${message}</div>", "Hola ${name}");
        when(templateEmailGateway.findTemplateEmail(any()))
                .thenReturn(Mono.just(template));
        when(generatorTokenUseCase.getToken(any(), any()))
                .thenReturn(Mono.just(new Mail()));
        when(generatorTokenUseCase.getNameToken(any()))
                .thenReturn(Mono.just("NameToken"));
        when(masivianGateway.sendMAIL(any()))
                .thenReturn(Mono.error(new Throwable("401 error")));
        StepVerifier
                .create(useCase.sendAlert(alert))
                .expectError()
                .verify();
    }

    @Test
    void sendAlertSesTest() {
        alert.setProvider("TOD");
        TemplateEmail template =
                new TemplateEmail("subject", "<div>Hola ${message}</div>", "Hola ${name}");
        when(templateEmailGateway.findTemplateEmail(any()))
                .thenReturn(Mono.just(template));
        when(sesGateway.sendEmail(any(), any()))
                .thenReturn(Mono.just(Response.builder()
                        .code(200)
                        .description("success")
                        .build()));
        when(logUseCase.sendLog(any(), any(), anyString(), any()))
                .thenReturn(Mono.empty());
        StepVerifier
                .create(useCase.sendAlert(alert))
                .verifyComplete();
    }

    @Test
    void sendAlertMasivianPathAttachmentTest() {
        List<Attachment> attachmentList = new ArrayList<>();
        attachmentList.add(Attachment.builder().filename("test.pdf").type("Path").value("/path/to/file/test.pdf").build());
        alert.setAttachments(attachmentList);
        TemplateEmail template =
                new TemplateEmail("subject", "<div>Hola ${message}</div>", "Hola ${name}");
        when(templateEmailGateway.findTemplateEmail(any()))
                .thenReturn(Mono.just(template));
        when(masivianGateway.generatePresignedUrl(anyString())).thenReturn(Mono.just("presignedUrl"));
        when(generatorTokenUseCase.getNameToken(any()))
                .thenReturn(Mono.just("NameToken"));
        when(masivianGateway.sendMAIL(any()))
                .thenReturn(Mono.just(Response.builder()
                        .code(200)
                        .description("success")
                        .build()));
        when(logUseCase.sendLog(any(), any(), anyString(), any()))
                .thenReturn(Mono.empty());
        when(generatorTokenUseCase.getToken(any(), any()))
                .thenReturn(Mono.just(new Mail()));
        StepVerifier
                .create(useCase.sendAlert(alert))
                .verifyComplete();
    }

    @Test
    void sendAlertMasivianUrlAttachmentTest() {
        List<Attachment> attachmentList = new ArrayList<>();
        attachmentList.add(Attachment.builder().filename("test.pdf").type("Url").value("http://url/test.pdf").build());
        alert.setAttachments(attachmentList);
        TemplateEmail template =
                new TemplateEmail("subject", "<div>Hola ${message}</div>", "Hola ${name}");
        when(templateEmailGateway.findTemplateEmail(any()))
                .thenReturn(Mono.just(template));
        when(masivianGateway.sendMAIL(any()))
                .thenReturn(Mono.just(Response.builder()
                        .code(200)
                        .description("success")
                        .build()));
        when(logUseCase.sendLog(any(), any(), anyString(), any()))
                .thenReturn(Mono.empty());
        when(generatorTokenUseCase.getToken(any(), any()))
                .thenReturn(Mono.just(new Mail()));
        when(generatorTokenUseCase.getNameToken(any()))
                .thenReturn(Mono.just("NameToken"));
        StepVerifier
                .create(useCase.sendAlert(alert))
                .verifyComplete();
    }

    @Test
    void sendAlertMasivianBase64AttachmentTest() {
        List<Attachment> attachmentList = new ArrayList<>();
        attachmentList.add(Attachment.builder().filename("test.pdf").type("Base64").contentType("application/pdf").build());
        alert.setAttachments(attachmentList);
        TemplateEmail template =
                new TemplateEmail("subject", "<div>Hola ${message}</div>", "Hola ${name}");
        when(templateEmailGateway.findTemplateEmail(any()))
                .thenReturn(Mono.just(template));
        when(masivianGateway.sendMAIL(any()))
                .thenReturn(Mono.just(Response.builder()
                        .code(200)
                        .description("success")
                        .build()));
        when(logUseCase.sendLog(any(), any(), anyString(), any()))
                .thenReturn(Mono.empty());
        when(generatorTokenUseCase.getToken(any(), any()))
                .thenReturn(Mono.just(new Mail()));
        when(generatorTokenUseCase.getNameToken(any()))
                .thenReturn(Mono.just("NameToken"));
        StepVerifier
                .create(useCase.sendAlert(alert))
                .verifyComplete();
    }
}
