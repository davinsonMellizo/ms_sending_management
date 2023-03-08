package co.com.bancolombia.usecase.log;

import co.com.bancolombia.model.events.gateways.CommandGateway;
import co.com.bancolombia.model.log.Log;
import co.com.bancolombia.model.message.Alert;
import co.com.bancolombia.model.message.Response;
import co.com.bancolombia.model.message.TemplateEmail;
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
class LogUseCaseTest {
    @InjectMocks
    private LogUseCase logUseCase;
    @Mock
    private CommandGateway commandGateway;
    private Alert alert = new Alert();
    private TemplateEmail templateEmail = new TemplateEmail();
    private Response response = new Response();

    @BeforeEach
    public void init (){
        alert.setLogKey(UUID.randomUUID().toString());
        alert.setProvider("TOD");
        alert.setDestination(Alert.Destination.builder().toAddress("email").build());
        response.setCode(1);
        response.setDescription("description");
        when(commandGateway.sendCommanLogEmail(any())).thenReturn(Mono.just(new Log()));
    }

    @Test
    void putLogTest(){
        StepVerifier.create(logUseCase.sendLog(alert, templateEmail, "", response))
                .expectError();
    }

    @Test
    void putLogTestError(){
        response.setCode(200);
        StepVerifier.create(logUseCase.sendLog(alert, templateEmail, "", response))
                .verifyComplete();
    }
    @Test
    void putLogCerTestError(){
        response.setCode(0);
        StepVerifier.create(logUseCase.sendLog(alert, templateEmail, "", response))
                .verifyComplete();
    }

}
