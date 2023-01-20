package co.com.bancolombia.usecase.sendalert.operations;

import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.alert.gateways.AlertGateway;
import co.com.bancolombia.model.alerttransaction.AlertTransaction;
import co.com.bancolombia.model.alerttransaction.gateways.AlertTransactionGateway;
import co.com.bancolombia.model.client.Client;
import co.com.bancolombia.model.client.gateways.ClientGateway;
import co.com.bancolombia.model.consumer.Consumer;
import co.com.bancolombia.model.consumer.gateways.ConsumerGateway;
import co.com.bancolombia.model.message.Message;
import co.com.bancolombia.model.message.Parameter;
import co.com.bancolombia.model.message.Response;
import co.com.bancolombia.usecase.log.LogUseCase;
import co.com.bancolombia.usecase.sendalert.RouterProviderMailUseCase;
import co.com.bancolombia.usecase.sendalert.RouterProviderPushUseCase;
import co.com.bancolombia.usecase.sendalert.RouterProviderSMSUseCase;
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
import java.util.Map;

import static co.com.bancolombia.commons.enums.BusinessErrorMessage.AMOUNT_NOT_EXCEEDED;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SendAlertZeroUseCaseTest {
    @InjectMocks
    private SendAlertZeroUseCase sendAlertZeroUseCase;
    @Mock
    private AlertGateway alertGateway;
    @Mock
    private AlertTransactionGateway alertTransactionGateway;
    @Mock
    private RouterProviderMailUseCase routerProviderMailUseCase;
    @Mock
    private RouterProviderPushUseCase routerProviderPushUseCase;
    @Mock
    private RouterProviderSMSUseCase routerProviderSMSUseCase;
    @Mock
    private LogUseCase logUseCase;
    @Mock
    private ClientGateway clientGateway;
    @Mock
    private ConsumerGateway consumerGateway;
    @Mock
    private ValidateContactUseCase validateContactUseCase;
    @Mock
    private ValidateAmountUseCase validateAmountUseCase;

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
        message.setPush(true);
        message.setPhone("32158967");
        message.setPhoneIndicator("57");
        message.setMail("bancolombia@com.co");
        message.setAttachments(new ArrayList<>());
        message.setPreferences(new ArrayList<>());
        Map<String, String> parameters = new HashMap<>();
        parameters.put("name", "bancolombia");
        message.setParameters(parameters);
    }

    @Test
    void sendAlertIndicatorZeroPushTest(){
        Alert alert = Alert.builder()
                .id("AFI").obligatory(false)
                .push("SI").nature("MO")
                .message("${name}")
                .idProviderMail("TOD")
                .idRemitter(0)
                .basicKit(false)
                .build();
        when(clientGateway.findClientByIdentification(anyLong(), anyInt())).thenReturn(Mono.just(Client.builder()
                .idState(1)
                .build()));
        when(validateAmountUseCase.validateAmount(any(), any())).thenReturn(Mono.just(alert));
        when(validateContactUseCase.validateDataContact(any(), any())).thenReturn(Mono.just(message));
        when(consumerGateway.findConsumerById(anyString())).thenReturn(Mono.just(Consumer.builder().build()));
        when(routerProviderMailUseCase.routeAlertMail(any(), any())).thenReturn(Mono.empty());
        when(routerProviderPushUseCase.sendPush(any(), any())).thenReturn(Mono.just(new Response()));
        when(alertGateway.findAlertById(anyString())).thenReturn(Mono.just(alert));
        when(alertTransactionGateway.findAllAlertTransaction((Message) any()))
                .thenReturn(Flux.just(AlertTransaction.builder().idAlert("AFI").build()));
        StepVerifier.create(sendAlertZeroUseCase.indicatorZero(message))
                .verifyComplete();
    }

    @Test
    void sendAlertIndicatorZeroSmsTest(){
        Alert alert = Alert.builder()
                .id("AFI").obligatory(false)
                .push("NO").nature("MO")
                .message("${name}")
                .idProviderMail("TOD")
                .idRemitter(0)
                .basicKit(false)
                .build();
        when(clientGateway.findClientByIdentification(anyLong(), anyInt())).thenReturn(Mono.just(Client.builder()
                .idState(1)
                .build()));
        when(validateContactUseCase.validateDataContact(any(), any())).thenReturn(Mono.just(message));
        when(validateAmountUseCase.validateAmount(any(), any())).thenReturn(Mono.empty());
        when(consumerGateway.findConsumerById(anyString())).thenReturn(Mono.just(Consumer.builder().build()));
        when(routerProviderMailUseCase.routeAlertMail(any(), any())).thenReturn(Mono.empty());
        when(routerProviderSMSUseCase.routeAlertsSMS(any(), any())).thenReturn(Mono.empty());
        when(alertGateway.findAlertById(anyString())).thenReturn(Mono.just(alert));
        when(alertTransactionGateway.findAllAlertTransaction((Message) any()))
                .thenReturn(Flux.just(AlertTransaction.builder().idAlert("AFI").build()));
        StepVerifier.create(sendAlertZeroUseCase.indicatorZero(message))
                .verifyComplete();
    }

    @Test
    void errorValidateAmountTest(){
        Alert alert = Alert.builder()
                .id("AFI").obligatory(false)
                .push("NO").nature("MO")
                .message("${name}")
                .idProviderMail("TOD")
                .idRemitter(0)
                .basicKit(false)
                .build();
        when(clientGateway.findClientByIdentification(anyLong(), anyInt())).thenReturn(Mono.just(Client.builder()
                .idState(1)
                .build()));
        when(validateAmountUseCase.validateAmount(any(), any()))
                .thenReturn(Mono.error(new BusinessException(AMOUNT_NOT_EXCEEDED)));
        when(validateContactUseCase.validateDataContact(any(), any())).thenReturn(Mono.just(message));
        when(consumerGateway.findConsumerById(anyString())).thenReturn(Mono.just(Consumer.builder().build()));
        when(alertGateway.findAlertById(anyString())).thenReturn(Mono.just(alert));
        when(alertTransactionGateway.findAllAlertTransaction((Message) any()))
                .thenReturn(Flux.just(AlertTransaction.builder().idAlert("AFI").build()));
        StepVerifier.create(sendAlertZeroUseCase.indicatorZero(message))
                .expectError().verify();
    }

    @Test
    void errorConsumerNotFoundTest(){
        Alert alert = Alert.builder()
                .id("AFI").obligatory(false)
                .push("NO").nature("MO")
                .message("${name}")
                .idProviderMail("TOD")
                .idRemitter(0)
                .basicKit(false)
                .build();
        when(clientGateway.findClientByIdentification(anyLong(), anyInt())).thenReturn(Mono.just(Client.builder()
                .idState(1)
                .build()));
        when(consumerGateway.findConsumerById(anyString())).thenReturn(Mono.empty());
        StepVerifier.create(sendAlertZeroUseCase.indicatorZero(message))
                .expectError()
                .verify();
    }

    @Test
    void errorClientInactiveTest(){
        when(clientGateway.findClientByIdentification(anyLong(), anyInt())).thenReturn(Mono.just(Client.builder()
                .idState(0)
                .build()));
        StepVerifier.create(sendAlertZeroUseCase.indicatorZero(message))
                .expectError().verify();
    }

    @Test
    void errorClientNotFountTest(){
        when(clientGateway.findClientByIdentification(anyLong(), anyInt())).thenReturn(Mono.empty());
        StepVerifier.create(sendAlertZeroUseCase.indicatorZero(message))
                .expectError().verify();
    }


}
