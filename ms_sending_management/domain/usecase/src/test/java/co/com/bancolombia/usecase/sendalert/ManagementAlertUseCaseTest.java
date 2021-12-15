package co.com.bancolombia.usecase.sendalert;

import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.alert.gateways.AlertGateway;
import co.com.bancolombia.model.events.gateways.CommandGateway;
import co.com.bancolombia.model.message.Message;
import co.com.bancolombia.model.message.Parameter;
import co.com.bancolombia.model.message.Response;
import co.com.bancolombia.usecase.log.LogUseCase;
import co.com.bancolombia.usecase.sendalert.operations.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ManagementAlertUseCaseTest {
    @InjectMocks
    private ManagementAlertUseCase managementAlertUseCase;
    @Mock
    private LogUseCase logUseCase;
    @Mock
    private AlertGateway alertGateway;
    @Mock
    private CommandGateway commandGateway;
    @Mock
    private SendAlertZeroUseCase sendAlertZeroUseCase;
    @Mock
    private SendAlertOneUseCase sendAlertOneUseCase;
    @Mock
    private SendAlertTwoUseCase sendAlertTwoUseCase;
    @Mock
    private SendAlertThreeUseCase sendAlertThreeUseCase;
    @Mock
    private SendAlertFiveUseCase sendAlertFiveUseCase;

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

    public void testCaseIndicatorOne(){
        Alert alert = Alert.builder()
                .push("SI")
                .build();
        Response response = Response.builder().code(202).description("success").build();
        /*when(alertGateway.findAlertById(anyString())).thenReturn(Mono.just(alert));
        when(logUseCase.sendLogPush(any(), any() ,anyString(), any())).thenReturn(Mono.just(response));*/
        /*when(logUseCase.sendLogError(any(),anyString(), any())).thenReturn(Mono.empty());*/
        when(sendAlertOneUseCase.sendAlertIndicatorOne(any())).thenReturn(Mono.empty());
        StepVerifier.create(managementAlertUseCase.alertSendingManager(message))
                .verifyComplete();
    }
}
