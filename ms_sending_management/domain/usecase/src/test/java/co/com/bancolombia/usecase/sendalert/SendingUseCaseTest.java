package co.com.bancolombia.usecase.sendalert;

import co.com.bancolombia.model.message.Message;
import co.com.bancolombia.usecase.log.LogUseCase;
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
class SendingUseCaseTest {
    @InjectMocks
    private SendingUseCase sendingUseCase;
    @Mock
    private LogUseCase logUseCase;

    private Message message = new Message();


    @Test
    void alertSendingManager(){
        message.setOperation(0);
        //when(sendAlertZeroUseCase.indicatorZero(any())).thenReturn(Mono.empty());
        StepVerifier.create(sendingUseCase.alertSendingManager(message))
                .verifyComplete();
    }

    @Test
    void alertSendingManagerError(){
        message.setOperation(10);
        when(logUseCase.sendLogError(any(),anyString(), any())).thenReturn(Mono.empty());
        StepVerifier.create(sendingUseCase.alertSendingManager(message))
                .verifyComplete();
    }

}
