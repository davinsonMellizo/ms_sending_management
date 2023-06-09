package co.com.bancolombia.usecase.sendalert;

import co.com.bancolombia.model.message.*;
import co.com.bancolombia.model.message.gateways.InalambriaGateway;
import co.com.bancolombia.model.message.gateways.InfobipGateway;
import co.com.bancolombia.model.message.gateways.MasivianGateway;
import co.com.bancolombia.model.message.gateways.TemplateGateway;
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
import java.util.Arrays;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
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

    @Mock
    private InfobipGateway infobipGateway;
    @Mock
    private  TemplateGateway templateGateway;

    private Alert alert = new Alert();
    private TemplateSms templateSms =new TemplateSms();
    private Response response = new Response();


    @BeforeEach
    public void init() {
        alert.setDestination(Alert.Destination.builder().phoneNumber("number").prefix("123").build());
        alert.setUrlForShortening("URl");
        alert.setProvider("MAS");
        ArrayList<Parameter> parameters = new ArrayList<>();
        parameters.add(new Parameter("name", "bancolombia", ""));
        alert.setMessage("text to send");
        alert.setTrackId(UUID.randomUUID().toString());
        templateSms.setBodyText("Message ");
        response.setCode(1);
        response.setDescription("description");

    }

    @Test
    void senAlert (){
        when(generatorTokenUseCase.getTokenMAS(any(), any()))
                .thenReturn(Mono.just(SMSMasiv.builder().customData("test").isFlash(false)
                        .isLongmessage(false).isPremium(false).text("textTest").to("123456789").build()));
        when(masivianGateway.sendSMS(any()))
                .thenReturn(Mono.just(Response.builder()
                        .code(200)
                        .description("success")
                        .build()));
        when(logUseCase.handlerLog(any(), anyString(), any(),anyBoolean()))
                .thenReturn(Mono.just(Response.builder()
                        .code(200)
                        .description("success")
                        .build()));
        when(templateGateway.findTemplateEmail(any()))
                .thenReturn(Mono.just(TemplateSms.builder().bodyText("Mensaje de prueba ").build()));
        StepVerifier
                .create(useCase.sendAlert(alert))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void sendAlertMasivianTest() {
        when(generatorTokenUseCase.getTokenMAS(any(), any()))
                .thenReturn(Mono.just(SMSMasiv.builder().customData("test").isFlash(false)
                        .isLongmessage(false).isPremium(false).text("textTest").to("123456789").build()));
        when(masivianGateway.sendSMS(any()))
                .thenReturn(Mono.just(Response.builder()
                        .code(200)
                        .description("success")
                        .build()));
        when(logUseCase.handlerLog(any(), anyString(), any(),anyBoolean()))
                .thenReturn(Mono.just(Response.builder()
                        .code(200)
                        .description("success")
                        .build()));
        StepVerifier
                .create(useCase.sendAlertToProviders(alert,templateSms))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void sendAlertInalambriaTest() {
        alert.setProvider("INA");
        when(generatorTokenUseCase.getTokenINA(any(), any()))
                .thenReturn(Mono.just(SMSInalambria.builder().DateMessage(LocalDateTime.now())
                        .Devices("DevicesTest").FlashSMS(123).HasMore(1234).MessageData("MessageDataTest")
                        .MessagePattern("MessagePatternTest").MessageText("MessageTextTest").TemplateId(12345)
                        .TransactionNumber(2).Type(1).Url("UrlTest").build()));
        when(inalambriaGateway.sendSMS(any()))
                .thenReturn(Mono.just(Response.builder()
                        .code(200)
                        .description("success")
                        .build()));
        when(logUseCase.handlerLog(any(), anyString(), any(),anyBoolean()))
                .thenReturn(Mono.just(Response.builder()
                        .code(200)
                        .description("success")
                        .build()));
        StepVerifier
                .create(useCase.sendAlertToProviders(alert,templateSms))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void sendAlertInfobipTest() {
        alert.setProvider("INF");
        when(generatorTokenUseCase.getTokenInf(any(), any()))
                .thenReturn(Mono.just(SMSInfobip.builder()
                        .messages(Arrays.asList(SMSInfobip.Message.builder()
                                .from("text")
                                .destinations(Arrays.asList(SMSInfobip.Destination.builder().to("1234").build()))
                                        .text("hi world").build())).build()));
        when(infobipGateway.sendSMS(any()))
                .thenReturn(Mono.just(Response.builder()
                        .code(200)
                        .description("success")
                        .build()));
        when(logUseCase.handlerLog(any(), anyString(), any(),anyBoolean()))
                .thenReturn(Mono.just(Response.builder()
                        .code(200)
                        .description("success")
                        .build()));
        StepVerifier
                .create(useCase.sendAlertToProviders(alert,templateSms))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void sendAlertMasivianError401Test() {
        when(generatorTokenUseCase.getTokenMAS(any(), any()))
                .thenReturn(Mono.just(SMSMasiv.builder().customData("test").isFlash(false)
                        .isLongmessage(false).isPremium(false).text("textTest").to("123456789").build()));
        when(masivianGateway.sendSMS(any()))
                .thenReturn(Mono.error(new Throwable("401 error")));
        StepVerifier
                .create(useCase.sendAlertToProviders(alert,templateSms))
                .expectError()
                .verify();
    }

    @Test
    void sendAlertInalambriaError401Test() {
        alert.setProvider("INA");
        when(generatorTokenUseCase.getTokenINA(any(), any()))
                .thenReturn(Mono.just(SMSInalambria.builder().DateMessage(LocalDateTime.now())
                        .Devices("DevicesTest").FlashSMS(123).HasMore(1234).MessageData("MessageDAtaTes")
                        .MessagePattern("MessagePatternTest").MessageText("MessageTextTest").TemplateId(12345)
                        .TransactionNumber(2).Type(1).Url("UrlTest").build()));
        when(inalambriaGateway.sendSMS(any()))
                .thenReturn(Mono.error(new Throwable("401 error")));
        StepVerifier
                .create(useCase.sendAlertToProviders(alert,templateSms))
                .expectError()
                .verify();
    }

    @Test
    void sendAlertInalambriaError500Test() {
        alert.setProvider("INA");
        when(generatorTokenUseCase.getTokenINA(any(), any()))
                .thenReturn(Mono.just(SMSInalambria.builder().DateMessage(LocalDateTime.now())
                        .Devices("DevicesTest").FlashSMS(123).HasMore(1234).MessageData("MessageDAtaTes")
                        .MessagePattern("MessagePatternTest").MessageText("MessageTextTest").TemplateId(12345)
                        .TransactionNumber(2).Type(1).Url("UrlTest").build()));
        when(inalambriaGateway.sendSMS(any()))
                .thenReturn(Mono.error(new Throwable("500 error")));
        StepVerifier
                .create(useCase.sendAlertToProviders(alert,templateSms))
                .expectError()
                .verify();
    }

    @Test
    void sendAlertInfobipError401Test() {
        alert.setProvider("INF");
        when(generatorTokenUseCase.getTokenInf(any(), any()))
                .thenReturn(Mono.just(SMSInfobip.builder()
                        .messages(Arrays.asList(SMSInfobip.Message.builder()
                                .from("text")
                                .destinations(Arrays.asList(SMSInfobip.Destination.builder().to("1234").build()))
                                .text("hi world").build())).build()));
        when(infobipGateway.sendSMS(any()))
                .thenReturn(Mono.error(new Throwable("401 error")));
        StepVerifier
                .create(useCase.sendAlertToProviders(alert,templateSms))
                .expectError()
                .verify();
    }
}
