package co.com.bancolombia.usecase.sendalert.routers;

import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.alert.gateways.AlertGateway;
import co.com.bancolombia.model.alertclient.AlertClient;
import co.com.bancolombia.model.alertclient.gateways.AlertClientGateway;
import co.com.bancolombia.model.message.Message;
import co.com.bancolombia.usecase.sendalert.AlertUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AlertUseCaseTest {
    @InjectMocks
    private AlertUseCase alertUseCase;
    @Mock
    private AlertClientGateway alertClientGateway;
    @Mock
    private AlertGateway alertGateway;

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
        message.setAttachments(new ArrayList<>());
        Map<String, String> parameters = new HashMap<>();
        parameters.put("name", "bancolombia");
        message.setParameters(parameters);
    }


    @Test
    void validateAmountErrorTest(){
        Alert alert = Alert.builder()
                .id("AFI")
                .push("SI")
                .providerMail("TOD")
                .remitter("bancolombia@bancolombia.com.co")
                .build();
        AlertClient alertClient = AlertClient.builder()
                .transactionDate(LocalDateTime.now()).accumulatedAmount(0L).accumulatedOperations(0)
                .amountEnable(100L).numberOperations(5)
                .build();
        when(alertClientGateway.findAlertClient(any())).thenReturn(Mono.just(alertClient));
        when(alertClientGateway.accumulate(any())).thenReturn(Mono.just(alertClient));
        StepVerifier.create(alertUseCase.validateAmount(alert, message))
                .expectError()
                .verify();
    }

    @Test
    void validateAmountTest(){
        Alert alert = Alert.builder()
                .id("AFI")
                .push("SI")
                .providerMail("TOD")
                .remitter("bancolombia@bancolombia.com.co")
                .build();
        AlertClient alertClient = AlertClient.builder()
                .transactionDate(LocalDateTime.now()).accumulatedAmount(60000L).accumulatedOperations(0)
                .amountEnable(1000L).numberOperations(5)
                .build();
        when(alertClientGateway.findAlertClient(any())).thenReturn(Mono.just(alertClient));
        when(alertClientGateway.accumulate(any())).thenReturn(Mono.just(alertClient));
        StepVerifier.create(alertUseCase.validateAmount(alert, message))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void getAlertTest(){
        Alert alert = Alert.builder()
                .id("AFI")
                .push("SI")
                .providerMail("TOD")
                .remitter("bancolombia@bancolombia.com.co")
                .build();
        when(alertGateway.findAlertById(anyString())).thenReturn(Mono.just(alert));
        StepVerifier.create(alertUseCase.getAlert(message))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void getAlertGnrTest(){
        Alert alert = Alert.builder()
                .id("AFI")
                .push("SI")
                .providerMail("TOD")
                .remitter("bancolombia@bancolombia.com.co")
                .build();
        Map<String, String> parameters = new HashMap<>();
        parameters.put("mensaje", "bancolombia");
        message.setParameters(parameters);
        message.setAlert("");
        when(alertGateway.findAlertById(anyString())).thenReturn(Mono.just(alert));
        StepVerifier.create(alertUseCase.getAlert(message))
                .expectNextCount(1)
                .verifyComplete();
    }


}
