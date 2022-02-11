package co.com.bancolombia.usecase.sendalert;

import co.com.bancolombia.model.message.*;
import co.com.bancolombia.model.message.gateways.MasivianGateway;
import co.com.bancolombia.model.message.gateways.PinpointGateway;
import co.com.bancolombia.model.message.gateways.SesGateway;
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
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SendAlertUseCaseTest {

    @InjectMocks
    private SendAlertUseCase useCase;
    @Mock
    private PinpointGateway pinpointGateway;
    @Mock
    private LogUseCase logUseCase;
    @Mock
    private MasivianGateway masivianGateway;
    @Mock
    private SesGateway sesGateway;
    private Alert alert = new Alert();

    @BeforeEach
    public void init() {
        alert.setProvider("MAS");
        alert.setFrom("bancolombia@test.com.co");
        alert.setDestination(new Alert.Destination("bancolombia@test.com.co","",""));
        alert.setAttachments(new ArrayList<>());
        ArrayList<Parameter> parameters = new ArrayList<>();
        parameters.add(new Parameter("name","bancolombia",""));
        alert.setTemplate(new Template(parameters, "Compra"));
        alert.setLogKey(UUID.randomUUID().toString());
    }

    @Test
    public void sendAlertMasivianTest(){
        TemplateEmail template =
                new TemplateEmail("subject","<div>Hola ${message}</div>","Hola ${name}");
        when(pinpointGateway.findTemplateEmail(anyString()))
                .thenReturn(Mono.just(template));
        when(masivianGateway.sendMAIL(any()))
                .thenReturn(Mono.just(Response.builder()
                        .code(200)
                        .description("success")
                        .build()));
        when(logUseCase.sendLog(any(), any(),anyString(), any()))
                .thenReturn(Mono.empty());
        StepVerifier
                .create(useCase.sendAlert(alert))
                .verifyComplete();
    }

    @Test
    public void sendAlertSesTest(){
        alert.setProvider("TOD");
        TemplateEmail template =
                new TemplateEmail("subject","<div>Hola ${message}</div>","Hola ${name}");
        when(pinpointGateway.findTemplateEmail(anyString()))
                .thenReturn(Mono.just(template));
        when(sesGateway.sendEmail(any(), any()))
                .thenReturn(Mono.just(Response.builder()
                        .code(200)
                        .description("success")
                        .build()));
        when(logUseCase.sendLog(any(), any(),anyString(), any()))
                .thenReturn(Mono.empty());
        StepVerifier
                .create(useCase.sendAlert(alert))
                .verifyComplete();
    }

}
