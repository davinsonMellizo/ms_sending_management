package co.com.bancolombia.usecase.sendalert;

import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.events.gateways.CommandGateway;
import co.com.bancolombia.model.message.Message;
import co.com.bancolombia.model.message.Response;
import co.com.bancolombia.model.provider.gateways.ProviderGateway;
import co.com.bancolombia.model.remitter.gateways.RemitterGateway;
import co.com.bancolombia.usecase.log.LogUseCase;
import co.com.bancolombia.usecase.sendalert.routers.RouterProviderMailUseCase;
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
class RouterProviderMailUseCaseTest {
    @InjectMocks
    private RouterProviderMailUseCase routerProviderMailUseCase;
    @Mock
    private RemitterGateway remitterGateway;
    @Mock
    private ProviderGateway providerGateway;
    @Mock
    private CommandGateway commandGateway;
    @Mock
    private LogUseCase logUseCase;

    private Message message = new Message();

    @BeforeEach
    public void init() {
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
        message.setRemitter("bancolombia@bancolombia.com.co");
        message.setPriority(2);
        message.setTemplate("compra");
        message.setAttachments(new ArrayList<>());
        Map<String, String> parameters = new HashMap<>();
        parameters.put("name", "bancolombia");
        message.setParameters(parameters);
        message.setPreferences(new ArrayList<>());
    }


    @Test
    void routeAlertMailTest() {
        Alert alert = Alert.builder()
                .push("SI")
                .templateName("template")
                .providerMail("TOD")
                .remitter("davinson@bancolombia.com.co")
                .build();
        when(logUseCase.sendLogMAIL(any(), any(), anyString(), any())).thenReturn(Mono.just(new Response()));
        when(commandGateway.sendCommandAlertEmail(any())).thenReturn(Mono.empty());

        StepVerifier.create(routerProviderMailUseCase.routeAlertMail(message, alert))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void routeAlertMailErrorTest() {
        Alert alert = Alert.builder()
                .push("SI")
                .providerMail("TOD")
                .remitter("bancolombia@bancolombia.com.co")
                .build();
        when(logUseCase.sendLogMAIL(any(), any(), anyString(), any())).thenReturn(Mono.just(new Response()));
        StepVerifier.create(routerProviderMailUseCase.routeAlertMail(message, alert))
                .expectNextCount(1)
                .verifyComplete();
    }
}
