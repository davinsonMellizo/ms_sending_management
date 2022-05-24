package co.com.bancolombia.usecase.sendalert;

import co.com.bancolombia.model.message.*;
import co.com.bancolombia.model.message.gateways.InalambriaGateway;
import co.com.bancolombia.model.message.gateways.MasivianGateway;
import co.com.bancolombia.usecase.log.LogUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SendAlertUseCaseTest {

    @InjectMocks
    private SendAlertUseCase useCase;
    @Mock
    private LogUseCase logUseCase;
    @Mock
    private MasivianGateway masivianGateway;
    @Mock
    private GeneratorTokenUseCase generatorTokenUseCase;
    @Mock
    private InalambriaGateway inalambriaGateway;

    private Alert alert = new Alert();

    @BeforeEach
    public void init() {
        alert.setTo("3215982557");
        alert.setUrl("URl");
        alert.setProvider("MAS");
        ArrayList<Parameter> parameters = new ArrayList<>();
        parameters.add(new Parameter("name","bancolombia",""));
        alert.setMessage("text to send");
        alert.setLogKey(UUID.randomUUID().toString());

    }

    @Test
    void sendAlertMasivianTest(){
        when(generatorTokenUseCase.getTokenMAS(any(),any()))
                .thenReturn(Mono.just(SMSMasiv.builder().customData("test").isFlash(false)
                        .isLongmessage(false).isPremium(false).text("textTest").to("123456789").build()));
         when(masivianGateway.sendSMS(any()))
                .thenReturn(Mono.just(Response.builder()
                        .code(200)
                        .description("success")
                        .build()));
        when(logUseCase.sendLog(any(),anyString(), any()))
                .thenReturn(Mono.empty());
        StepVerifier
                .create(useCase.sendAlert(alert))
                .verifyComplete();
    }

    @Test
    void sendAlertInalambriaTest(){
        alert.setProvider("INA");
        when(generatorTokenUseCase.getTokenINA(any(),any()))
                .thenReturn(Mono.just(SMSInalambria.builder().DateMessage(LocalDateTime.now())
                        .Devices("DevicesTest").FlashSMS(123).HasMore(1234).MessageData("MessageDataTest")
                        .MessagePattern("MessagePatternTest").MessageText("MessageTextTest").TemplateId(12345)
                        .TransactionNumber(2).Type(1).Url("UrlTest").build()));
        when(inalambriaGateway.sendSMS(any()))
                .thenReturn(Mono.just(Response.builder()
                        .code(200)
                        .description("success")
                        .build()));
        when(logUseCase.sendLog(any(),anyString(), any()))
                .thenReturn(Mono.empty());
        StepVerifier
                .create(useCase.sendAlert(alert))
                .verifyComplete();
    }

    @Test
    void sendAlertMasivianError401Test(){
        when(generatorTokenUseCase.getTokenMAS(any(),any()))
                .thenReturn(Mono.just(SMSMasiv.builder().customData("test").isFlash(false)
                        .isLongmessage(false).isPremium(false).text("textTest").to("123456789").build()));
        when(masivianGateway.sendSMS(any()))
                .thenReturn(Mono.error(new Throwable("401 error")));
        StepVerifier
                .create(useCase.sendAlert(alert))
                .expectError()
                .verify();
    }

    @Test
    void sendAlertInalambriaError401Test(){
        alert.setProvider("INA");
        when(generatorTokenUseCase.getTokenINA(any(),any()))
                .thenReturn(Mono.just(SMSInalambria.builder().DateMessage(LocalDateTime.now())
                        .Devices("DevicesTest").FlashSMS(123).HasMore(1234).MessageData("MessageDAtaTes")
                        .MessagePattern("MessagePatternTest").MessageText("MessageTextTest").TemplateId(12345)
                        .TransactionNumber(2).Type(1).Url("UrlTest").build()));
        when(inalambriaGateway.sendSMS(any()))
                .thenReturn(Mono.error(new Throwable("401 error")));
        StepVerifier
                .create(useCase.sendAlert(alert))
                .expectError()
                .verify();
    }
    @Test
    void sendAlertInalambriaError500Test(){
        alert.setProvider("INA");
        when(generatorTokenUseCase.getTokenINA(any(),any()))
                .thenReturn(Mono.just(SMSInalambria.builder().DateMessage(LocalDateTime.now())
                        .Devices("DevicesTest").FlashSMS(123).HasMore(1234).MessageData("MessageDAtaTes")
                        .MessagePattern("MessagePatternTest").MessageText("MessageTextTest").TemplateId(12345)
                        .TransactionNumber(2).Type(1).Url("UrlTest").build()));
        when(inalambriaGateway.sendSMS(any()))
                .thenReturn(Mono.error(new Throwable("500 error")));
        StepVerifier
                .create(useCase.sendAlert(alert))
                .expectError()
                .verify();
    }
}
