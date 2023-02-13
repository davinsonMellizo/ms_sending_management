package co.com.bancolombia.usecase.sendalert;

import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.events.gateways.CommandGateway;
import co.com.bancolombia.model.message.Message;
import co.com.bancolombia.model.provider.Provider;
import co.com.bancolombia.model.provider.gateways.ProviderGateway;
import co.com.bancolombia.model.remitter.Remitter;
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
import static org.mockito.ArgumentMatchers.anyInt;
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
    void routeAlertMailTest(){
        Remitter remitter = Remitter.builder().mail("bancolombia@com.co").build();
        Provider provider = Provider.builder().id("MAS").build();
        Alert alert = Alert.builder()
                .push("SI")
                .templateName("template")
                .idProviderMail("TOD")
                .idRemitter(0)
                .build();
        when(logUseCase.sendLogMAIL(any(),any(), anyString(), any())).thenReturn(Mono.empty());
        when(providerGateway.findProviderById(anyString())).thenReturn(Mono.just(provider));
        when(remitterGateway.findRemitterById(anyInt())).thenReturn(Mono.just(remitter));
        when(commandGateway.sendCommandAlertEmail(any())).thenReturn(Mono.empty());

        StepVerifier.create(routerProviderMailUseCase.routeAlertMail(message, alert))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void routeAlertMailErrorTest(){
        Provider provider = Provider.builder().id("MAS").build();
        Alert alert = Alert.builder()
                .push("SI")
                .idProviderMail("TOD")
                .idRemitter(0)
                .build();
        when(logUseCase.sendLogMAIL(any(),any(), anyString(), any())).thenReturn(Mono.empty());
        when(providerGateway.findProviderById(anyString())).thenReturn(Mono.just(provider));
        StepVerifier.create(routerProviderMailUseCase.routeAlertMail(message, alert))
                .expectNextCount(1)
                .verifyComplete();
    }
}
