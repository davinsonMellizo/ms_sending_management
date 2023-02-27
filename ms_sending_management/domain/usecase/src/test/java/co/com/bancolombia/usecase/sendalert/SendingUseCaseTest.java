package co.com.bancolombia.usecase.sendalert;

import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.message.Message;
import co.com.bancolombia.model.message.Response;
import co.com.bancolombia.usecase.log.LogUseCase;
import co.com.bancolombia.usecase.sendalert.routers.RouterProviderMailUseCase;
import co.com.bancolombia.usecase.sendalert.routers.RouterProviderPushUseCase;
import co.com.bancolombia.usecase.sendalert.routers.RouterProviderSMSUseCase;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SendingUseCaseTest {
    @InjectMocks
    private SendingUseCase sendingUseCase;
    @Mock
    private LogUseCase logUseCase;
    @Mock
    private ClientUseCase clientUseCase;
    @Mock
    private AlertUseCase alertUseCase;
    @Mock
    private RouterProviderPushUseCase routerProviderPushUseCase;
    @Mock
    private RouterProviderMailUseCase routerProviderMailUseCase;
    @Mock
    private RouterProviderSMSUseCase routerProviderSMSUseCase;

    private Message message = new Message();
    private Alert alert;


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
        message.setPhone("32158967");
        message.setPhoneIndicator("57");
        message.setMail("bancolombia@com.co");
        message.setAttachments(new ArrayList<>());
        Map<String, String> parameters = new HashMap<>();
        parameters.put("Name", "bancolombia");
        message.setParameters(parameters);

        alert = Alert.builder()
                .idState(1)
                .nature("MO")
                .message("${Name}")
                .basicKit(false)
                .obligatory(false)
                .build();
    }

    @Test
    void alertSendingManager(){
        Response response = new Response();
        when(routerProviderMailUseCase.routeAlertMail(any(), any())).thenReturn(Mono.just(response));
        when(routerProviderPushUseCase.routeAlertPush(any(), any())).thenReturn(Mono.just(response));
        when(routerProviderSMSUseCase.routeAlertsSMS(any(), any())).thenReturn(Mono.just(response));
        when(alertUseCase.validateAmount(any(), any())).thenReturn(Mono.empty());
        when(alertUseCase.getAlert(any())).thenReturn(Mono.just(alert));
        when(alertUseCase.getAlertsByTransactions(any())).thenReturn(Flux.just(alert));
        when(clientUseCase.validateClientInformation(any())).thenReturn(Mono.just(message));

        StepVerifier.create(sendingUseCase.alertSendingManager(List.of(message)))
                .verifyComplete();
    }

    @Test
    void alertSendingManagerByAlert(){
        message.setTransactionCode("");
        Response response = new Response();
        when(routerProviderMailUseCase.routeAlertMail(any(), any())).thenReturn(Mono.just(response));
        when(routerProviderPushUseCase.routeAlertPush(any(), any())).thenReturn(Mono.just(response));
        when(routerProviderSMSUseCase.routeAlertsSMS(any(), any())).thenReturn(Mono.just(response));
        when(alertUseCase.validateAmount(any(), any())).thenReturn(Mono.empty());
        when(alertUseCase.getAlert(any())).thenReturn(Mono.just(alert));
        when(clientUseCase.validateClientInformation(any())).thenReturn(Mono.just(message));

        StepVerifier.create(sendingUseCase.alertSendingManager(List.of(message)))
                .verifyComplete();
    }
}
