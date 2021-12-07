package co.com.bancolombia.usecase.sendalert;

import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.events.gateways.CommandGateway;
import co.com.bancolombia.model.message.Message;
import co.com.bancolombia.model.message.Parameter;
import co.com.bancolombia.model.provider.Provider;
import co.com.bancolombia.model.provider.gateways.ProviderGateway;
import co.com.bancolombia.model.remitter.Remitter;
import co.com.bancolombia.model.remitter.gateways.RemitterGateway;
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
public class RouterProviderMailUseCaseTest {
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
    public void routeAlertMailTest(){
        Remitter remitter = Remitter.builder().mail("bancolombia@com.co").build();
        Provider provider = Provider.builder().id("MAS").build();
        Alert alert = Alert.builder()
                .push("SI")
                .idProviderMail(0)
                .idRemitter(0)
                .build();
        when(commandGateway.sendCommandAlertEmail(any())).thenReturn(Mono.empty());
        when(logUseCase.sendLogMAIL(any(),any(), anyString(), any())).thenReturn(Mono.empty());
        when(remitterGateway.findRemitterById(anyInt())).thenReturn(Mono.just(remitter));
        when(providerGateway.findProviderByProviderService(anyInt())).thenReturn(Mono.just(provider));
        StepVerifier.create(routerProviderMailUseCase.routeAlertMail(message, alert))
                .verifyComplete();
    }

    @Test
    public void routeAlertMailErrorTest(){
        Remitter remitter = Remitter.builder().mail("bancolombia@com.co").build();
        Provider provider = Provider.builder().id("MAS").build();
        Alert alert = Alert.builder()
                .push("SI")
                .idProviderMail(0)
                .idRemitter(0)
                .build();
        when(commandGateway.sendCommandAlertEmail(any())).thenReturn(Mono.error(new Throwable("error")));
        when(logUseCase.sendLogMAIL(any(),any(), anyString(), any())).thenReturn(Mono.empty());
        when(remitterGateway.findRemitterById(anyInt())).thenReturn(Mono.just(remitter));
        when(providerGateway.findProviderByProviderService(anyInt())).thenReturn(Mono.just(provider));
        StepVerifier.create(routerProviderMailUseCase.routeAlertMail(message, alert))
                .expectError()
                .verify();
    }
}
