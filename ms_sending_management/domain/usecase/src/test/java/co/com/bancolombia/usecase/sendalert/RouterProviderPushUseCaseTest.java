package co.com.bancolombia.usecase.sendalert;

import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.message.Message;
import co.com.bancolombia.model.message.Parameter;
import co.com.bancolombia.model.message.Response;
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
    void routeAlertPushTest(){
        Alert alert = Alert.builder()
                .push("SI")
                .idProviderSms("ALM")
                .priority(0)
                .build();
        when(pushGateway.sendPush(any())).thenReturn(Mono.just(Response.builder()
                .description("success").code(200)
                .build()));
        when(logUseCase.sendLogPush(any(),any(), anyString(), any())).thenReturn(Mono.just(new Response()));
        StepVerifier.create(routerProviderPushUseCase.sendPush(message, alert))
                .expectNextCount(1)
                .verifyComplete();
    }

}