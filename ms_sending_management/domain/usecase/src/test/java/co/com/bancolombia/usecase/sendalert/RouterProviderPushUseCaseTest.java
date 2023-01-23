package co.com.bancolombia.usecase.sendalert;

import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.document.Document;
import co.com.bancolombia.model.document.gateways.DocumentGateway;
import co.com.bancolombia.model.message.Message;
import co.com.bancolombia.model.message.Response;
import co.com.bancolombia.model.message.gateways.PushGateway;
import co.com.bancolombia.usecase.log.LogUseCase;
import co.com.bancolombia.usecase.sendalert.routers.RouterProviderPushUseCase;
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
class RouterProviderPushUseCaseTest {
    @InjectMocks
    private RouterProviderPushUseCase routerProviderPushUseCase;
    @Mock
    private PushGateway pushGateway;
    @Mock
    private LogUseCase logUseCase;
    @Mock
    private DocumentGateway documentGateway;

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
        message.setApplicationCode("PERSONAS");
        message.setPhone("32158967");
        message.setPhoneIndicator("57");
        message.setMail("bancolombia@com.co");
        message.setAttachments(new ArrayList<>());
        message.setLogKey("testKey");
        message.setPush(true);
        ArrayList<String> preferences = new ArrayList<>();
        preferences.add("SMS");
        message.setPreferences(preferences);
        Map<String, String> parameters = new HashMap<>();
        parameters.put("name", "bancolombia");
        message.setParameters(parameters);
    }


    @Test
    void routeAlertPushTest(){
        Alert alert = Alert.builder()
                .push("SI")
                .idProviderSms("ALM")
                .priority(0)
                .idCategory(1)
                .build();
        when(pushGateway.sendPush(any())).thenReturn(Mono.just(Response.builder()
                .description("success").code(200)
                .build()));
        when(logUseCase.sendLogPush(any(),any(), anyString(), any())).thenReturn(Mono.just(new Response()));
        when(documentGateway.getDocument(anyString())).thenReturn(Mono.just(Document.builder().code("testCode").build()));
        StepVerifier.create(routerProviderPushUseCase.sendPush(message, alert))
                .expectNextCount(1)
                .verifyComplete();
    }

}
