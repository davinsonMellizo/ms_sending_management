package co.com.bancolombia.usecase.sendalert.validations;

import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.alert.gateways.AlertGateway;
import co.com.bancolombia.model.alerttransaction.AlertTransaction;
import co.com.bancolombia.model.alerttransaction.gateways.AlertTransactionGateway;
import co.com.bancolombia.model.message.Message;
import co.com.bancolombia.model.message.Parameter;
import co.com.bancolombia.model.message.Response;
import co.com.bancolombia.usecase.log.LogUseCase;
import co.com.bancolombia.usecase.sendalert.RouterProviderMailUseCase;
import co.com.bancolombia.usecase.sendalert.RouterProviderPushUseCase;
import co.com.bancolombia.usecase.sendalert.RouterProviderSMSUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SendAlertTwoUseCaseTest {
    @InjectMocks
    private SendAlertTwoUseCase sendAlertTwoUseCase;
    @Mock
    private AlertGateway alertGateway;
    @Mock
    private AlertTransactionGateway alertTransactionGateway;
    @Mock
    private RouterProviderMailUseCase routerProviderMailUseCase;
    @Mock
    private RouterProviderPushUseCase routerProviderPushUseCase;
    @Mock
    private RouterProviderSMSUseCase routerProviderSMSUseCase;
    @Mock
    private LogUseCase logUseCase;

    private Message message = new Message();

    @BeforeEach
    public void init(){
        message.setOperation(1);
        message.setDocumentType(0);
        message.setDocumentNumber(1061781558L);
        message.setConsumer("SVP");
        message.setAlert("AFI");
        message.setTransactionCode("580");
        message.setAmount(60000L);
        message.setUrl("");
        message.setPhone("32158967");
        message.setPhoneIndicator("57");
        message.setMail("bancolombia@com.co");
        message.setAttachments(new ArrayList<>());
        ArrayList<Parameter> parameters = new ArrayList<>();
        Parameter parameter = Parameter.builder().Name("name").Value("bancolombia").build();
        parameters.add(parameter);
        message.setParameters(parameters);
    }

    @Test
    public void sendAlertIndicatorTwoPushTest(){
        Alert alert = Alert.builder()
                .id("AFI")
                .push("SI")
                .idProviderMail(0)
                .idRemitter(0)
                .build();
        when(routerProviderMailUseCase.routeAlertMail(any(), any())).thenReturn(Mono.empty());
        when(routerProviderPushUseCase.sendPush(any(), any())).thenReturn(Mono.just(new Response()));
        when(routerProviderSMSUseCase.validateMobile(any(), any())).thenReturn(Mono.empty());
        when(alertGateway.findAlertById(anyString())).thenReturn(Mono.just(alert));
        when(alertTransactionGateway.findAllAlertTransaction((Message) any()))
                .thenReturn(Flux.just(AlertTransaction.builder().idAlert("AFI").build()));
        StepVerifier.create(sendAlertTwoUseCase.validateWithCodeTrx(message))
                .verifyComplete();
    }

    @Test
    public void sendAlertIndicatorTwoSmsTest(){
        Alert alert = Alert.builder()
                .id("AFI")
                .push("NO")
                .idProviderMail(0)
                .idRemitter(0)
                .build();
        when(routerProviderMailUseCase.routeAlertMail(any(), any())).thenReturn(Mono.empty());
        when(routerProviderSMSUseCase.validateMobile(any(), any())).thenReturn(Mono.empty());
        when(alertGateway.findAlertById(anyString())).thenReturn(Mono.just(alert));
        when(alertTransactionGateway.findAllAlertTransaction((Message) any()))
                .thenReturn(Flux.just(AlertTransaction.builder().idAlert("AFI").build()));
        StepVerifier.create(sendAlertTwoUseCase.validateWithCodeTrx(message))
                .verifyComplete();
    }

    @Test
    public void errorAlertTransactionNotFoundTest(){
        when(alertGateway.findAlertById(anyString())).thenReturn(Mono.empty());
        when(alertTransactionGateway.findAllAlertTransaction((Message) any()))
                .thenReturn(Flux.just(AlertTransaction.builder().idAlert("AFI").build()));
        StepVerifier.create(sendAlertTwoUseCase.validateWithCodeTrx(message))
                .expectError().verify();
    }

    @Test
    public void errorAlertNotFoundTest(){
        when(alertTransactionGateway.findAllAlertTransaction((Message) any()))
                .thenReturn(Flux.empty());
        StepVerifier.create(sendAlertTwoUseCase.validateWithCodeTrx(message))
                .expectError().verify();
    }


}
