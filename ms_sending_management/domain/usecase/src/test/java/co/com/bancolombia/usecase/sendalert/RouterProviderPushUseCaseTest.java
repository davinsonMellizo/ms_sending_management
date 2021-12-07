package co.com.bancolombia.usecase.sendalert;

import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.events.gateways.CommandGateway;
import co.com.bancolombia.model.message.Message;
import co.com.bancolombia.model.message.Parameter;
import co.com.bancolombia.model.message.Response;
import co.com.bancolombia.model.prefix.Prefix;
import co.com.bancolombia.model.prefix.gateways.PrefixRepository;
import co.com.bancolombia.model.priority.Priority;
import co.com.bancolombia.model.priority.gateways.PriorityGateway;
import co.com.bancolombia.model.provider.Provider;
import co.com.bancolombia.model.provider.gateways.ProviderGateway;
import co.com.bancolombia.model.providerservice.ProviderService;
import co.com.bancolombia.model.providerservice.gateways.ProviderServiceGateway;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RouterProviderPushUseCaseTest {
    @InjectMocks
    private RouterProviderPushUseCase routerProviderPushUseCase;
    @Mock
    private CommandGateway commandGateway;
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
    public void routeAlertPushTest(){
        Alert alert = Alert.builder()
                .push("SI")
                .idProviderSms(0)
                .priority(0)
                .build();
        when(commandGateway.sendCommandAlertSms(any())).thenReturn(Mono.empty());
        when(logUseCase.sendLogPush(any(),any(), anyString(), any())).thenReturn(Mono.just(new Response()));
        StepVerifier.create(routerProviderPushUseCase.sendPush(message, alert))
                .expectNextCount(1)
                .verifyComplete();
    }

}
