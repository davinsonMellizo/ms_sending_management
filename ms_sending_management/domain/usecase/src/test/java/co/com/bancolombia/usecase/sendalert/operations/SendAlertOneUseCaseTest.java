package co.com.bancolombia.usecase.sendalert.operations;

import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.alert.gateways.AlertGateway;
import co.com.bancolombia.model.message.Message;
import co.com.bancolombia.model.message.Parameter;
import co.com.bancolombia.model.message.Response;
import co.com.bancolombia.usecase.sendalert.RouterProviderMailUseCase;
import co.com.bancolombia.usecase.sendalert.RouterProviderPushUseCase;
import co.com.bancolombia.usecase.sendalert.RouterProviderSMSUseCase;
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
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SendAlertOneUseCaseTest {
    @InjectMocks
    private SendAlertOneUseCase sendAlertOneUseCase;
    @Mock
    private RouterProviderMailUseCase routerProviderMailUseCase;
    @Mock
    private RouterProviderPushUseCase routerProviderPushUseCase;
    @Mock
    private RouterProviderSMSUseCase routerProviderSMSUseCase;
    @Mock
    private AlertGateway alertGateway;

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
        Map<String, String> parameters = new HashMap<>();
        parameters.put("name", "bancolombia");
        message.setParameters(parameters);
    }

    @Test
    void sendAlertIndicatorOnePushTest(){
        Alert alert = Alert.builder()
                .id("AFI")
                .push("SI")
                .message("${name}")
                .idProviderMail("TOD")
                .idRemitter(0)
                .build();
        when(alertGateway.findAlertById(anyString())).thenReturn(Mono.just(alert));
        when(routerProviderMailUseCase.routeAlertMail(any(), any())).thenReturn(Mono.empty());
        when(routerProviderPushUseCase.sendPush(any(), any())).thenReturn(Mono.just(new Response()));
        when(routerProviderSMSUseCase.validateMobile(any(), any())).thenReturn(Mono.empty());
        StepVerifier.create(sendAlertOneUseCase.sendAlertIndicatorOne(message))
                .verifyComplete();
    }

    @Test
    void sendAlertIndicatorOneSMSTest(){
        Alert alert = Alert.builder()
                .id("AFI")
                .push("NO")
                .message("${name}")
                .idProviderMail("TOD")
                .idRemitter(0)
                .build();
        when(alertGateway.findAlertById(anyString())).thenReturn(Mono.just(alert));
        when(routerProviderMailUseCase.routeAlertMail(any(), any())).thenReturn(Mono.empty());
        when(routerProviderSMSUseCase.validateMobile(any(), any())).thenReturn(Mono.empty());
        StepVerifier.create(sendAlertOneUseCase.sendAlertIndicatorOne(message))
                .verifyComplete();
    }


}
