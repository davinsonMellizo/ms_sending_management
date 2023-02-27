package co.com.bancolombia.usecase.sendalert;

import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.events.gateways.CommandGateway;
import co.com.bancolombia.model.message.Message;
import co.com.bancolombia.model.message.Response;
import co.com.bancolombia.model.priority.Priority;
import co.com.bancolombia.model.priority.gateways.PriorityGateway;
import co.com.bancolombia.model.provider.Provider;
import co.com.bancolombia.model.provider.gateways.ProviderGateway;
import co.com.bancolombia.usecase.log.LogUseCase;
import co.com.bancolombia.usecase.sendalert.routers.RouterProviderSMSUseCase;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RouterProviderSMSUseCaseTest {
    @InjectMocks
    private RouterProviderSMSUseCase routerProviderSMSUseCase;

    @Mock
    private PriorityGateway priorityGateway;
    @Mock
    private ProviderGateway providerGateway;
    @Mock
    private LogUseCase logUseCase;
    @Mock
    private CommandGateway commandGateway;

    private Message message = new Message();

    @BeforeEach
    public void init(){
        message.setRetrieveInformation(true);
        message.setDocumentType(0);
        message.setDocumentNumber(1061781558L);
        message.setConsumer("SVP");
        message.setAlert("AFI");
        message.setTransactionCode("580");
        message.setAmount(60000L);
        message.setUrl("");
        message.setPhone("3210000000");
        message.setPhoneIndicator("57");
        message.setMail("bancolombia@com.co");
        message.setAttachments(new ArrayList<>());
        Map<String, String> parameters = new HashMap<>();
        parameters.put("name", "bancolombia");
        message.setParameters(parameters);
        ArrayList<String> preferences = new ArrayList<>();
        preferences.add("SMS");
        message.setPreferences(preferences);
    }


    @Test
    void routeAlertSmsTest(){
        Alert alert = Alert.builder()
                .push("NO")
                .providerSms("MAS")
                .priority(0)
                .build();
        when(logUseCase.sendLogSMS(any(), any(),anyString(), any())).thenReturn(Mono.just(new Response()));
        when(commandGateway.sendCommandAlertSms(any())).thenReturn(Mono.empty());

        StepVerifier.create(routerProviderSMSUseCase.routeAlertsSMS(message, alert))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void routeAlertSmsErrorTest(){
        Alert alert = Alert.builder()
                .push("SI")
                .providerSms("0")
                .priority(0)
                .build();
        StepVerifier.create(routerProviderSMSUseCase.routeAlertsSMS(message, alert))
                .expectError().verify();
    }

}
