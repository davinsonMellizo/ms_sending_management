package co.com.bancolombia.usecase.log;

import co.com.bancolombia.model.log.Log;
import co.com.bancolombia.model.log.gateways.LogGateway;
import co.com.bancolombia.model.message.Alert;
import co.com.bancolombia.model.message.Response;
import co.com.bancolombia.model.message.TemplateSms;
import co.com.bancolombia.usecase.log.LogUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LogUseCaseTest {
    @InjectMocks
    private LogUseCase logUseCase;
    @Mock
    private LogGateway logGateway;
    private Alert alert = new Alert();
    private TemplateSms templateEmail = new TemplateSms();
    private Response response = new Response();

    @BeforeEach
    public void init (){
        alert.setLogKey(UUID.randomUUID().toString());
        alert.setProvider("INA");
        alert.setTo("number");
        response.setCode(1);
        response.setDescription("description");
        templateEmail.setBodyText("text");
        when(logGateway.putLogToSQS(any())).thenReturn(Mono.just(new Log()));
    }

    @Test
    public void putLogTest(){
        StepVerifier.create(logUseCase.sendLog(alert, "", response))
                .expectError();
    }

}
