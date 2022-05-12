package co.com.bancolombia.usecase.log;

import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.log.Log;
import co.com.bancolombia.model.log.gateways.LogGateway;
import co.com.bancolombia.model.message.Message;
import co.com.bancolombia.model.message.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
class LogUseCaseTest {
    @InjectMocks
    private LogUseCase logUseCase;
    @Mock
    private LogGateway logGateway;
    private Message message = new Message();
    private Response response = new Response();
    private Alert alert = new Alert();

    @BeforeEach
    public void init (){
        message.setParameters(new ArrayList<>());
        when(logGateway.putLogToSQS(any())).thenReturn(Mono.just(new Log()));
    }

    @Test
     void putLogErrorTest(){
        StepVerifier.create(logUseCase.sendLogError(message, "", response))
                .verifyComplete();
    }

    @Test
    void putLogEmailTest(){
        StepVerifier.create(logUseCase.sendLogMAIL(message, alert, "", response))
                .verifyComplete();
    }

    @Test
    void putLogPushTest(){
        StepVerifier.create(logUseCase.sendLogPush(message, alert, "", response))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void putLogSmsErrorTest(){
        StepVerifier.create(logUseCase.sendLogSMS(message, alert, "", response))
                .verifyComplete();
    }

}
