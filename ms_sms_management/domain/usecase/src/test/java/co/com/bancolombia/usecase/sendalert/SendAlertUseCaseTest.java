package co.com.bancolombia.usecase.sendalert;

import co.com.bancolombia.model.message.*;
import co.com.bancolombia.model.message.gateways.InalambriaGateway;
import co.com.bancolombia.model.message.gateways.MasivianGateway;
import co.com.bancolombia.model.message.gateways.PinpointGateway;
import co.com.bancolombia.model.message.gateways.PushGateway;
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
    private PushGateway pushGateway;
    @Mock
    private InalambriaGateway inalambriaGateway;
    private Alert alert = new Alert();

    @BeforeEach
    public void init() {
        alert.setTo("3215982557");
        alert.setUrl("");
        alert.setProvider("MAS");
        ArrayList<Parameter> parameters = new ArrayList<>();
        parameters.add(new Parameter("name","bancolombia",""));
        alert.setTemplate(new Template(parameters, "Compra"));
        alert.setLogKey(UUID.randomUUID().toString());
        when(pinpointGateway.findTemplateSms(any())).thenReturn(Mono.just(new TemplateSms("Hola ${name}")));
    }

    @Test
    public void sendAlertMasivianTest(){
         when(masivianGateway.sendSMS(any()))
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
    public void sendAlertInalambriaTest(){
        alert.setProvider("INA");
        when(inalambriaGateway.sendSMS(any()))
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
    public void sendAlertPushTest(){
        alert.setProvider("PUSH");
       when(pushGateway.sendPush(any()))
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
    public void sendAlertMasivianErrorTest(){
        when(masivianGateway.sendSMS(any()))
                .thenReturn(Mono.error(new Throwable("error")));
        StepVerifier
                .create(useCase.sendAlert(alert))
                .expectError()
                .verify();
    }

    @Test
    public void sendAlertInalambriaErrorTest(){
        alert.setProvider("INA");
        when(inalambriaGateway.sendSMS(any()))
                .thenReturn(Mono.error(new Throwable("error")));
        StepVerifier
                .create(useCase.sendAlert(alert))
                .expectError()
                .verify();
    }

    @Test
    public void sendAlertPushErrorTest(){
        alert.setProvider("PUSH");
        when(pushGateway.sendPush(any()))
                .thenReturn(Mono.error(new Throwable("error")));
        StepVerifier
                .create(useCase.sendAlert(alert))
                .expectError()
                .verify();
    }

}
