package co.com.bancolombia.usecase.sendalert;

import co.com.bancolombia.model.alert.gateways.AlertGateway;
import co.com.bancolombia.model.events.gateways.CommandGateway;
import co.com.bancolombia.model.message.Message;
import co.com.bancolombia.usecase.log.LogUseCase;
import co.com.bancolombia.usecase.sendalert.operations.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ManagementAlertUseCaseTest {
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


    @Test
    void alertSendingManager(){
        message.setOperation(0);
        when(sendAlertZeroUseCase.sendAlertsIndicatorZero(any())).thenReturn(Mono.empty());
        StepVerifier.create(managementAlertUseCase.alertSendingManager(message))
                .verifyComplete();
    }

    @Test
    void alertSendingManagerError(){
        message.setOperation(10);
        when(logUseCase.sendLogError(any(),anyString(), any())).thenReturn(Mono.empty());
        StepVerifier.create(managementAlertUseCase.alertSendingManager(message))
                .verifyComplete();
    }

}
