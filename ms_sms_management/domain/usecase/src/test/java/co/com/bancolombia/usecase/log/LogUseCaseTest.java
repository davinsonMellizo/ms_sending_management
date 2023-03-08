package co.com.bancolombia.usecase.log;

import co.com.bancolombia.model.events.gateways.CommandGateway;
import co.com.bancolombia.model.log.Log;
import co.com.bancolombia.model.message.Alert;
import co.com.bancolombia.model.message.Response;
import co.com.bancolombia.model.message.TemplateSms;
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
    private TemplateSms templateEmail = new TemplateSms();
    private Response response = new Response();

    @BeforeEach
    public void init() {
        alert.setTrackId(UUID.randomUUID().toString());
        alert.setProvider("INA");
        alert.setDestination(Alert.To.builder().phoneNumber("number").prefix("123").build());
        response.setCode(1);
        response.setDescription("description");
        templateEmail.setBodyText("text");

    }

    @Test
    void putLogTest() {
        when(commandGateway.sendCommandLogSms(any())).thenReturn(Mono.just(new Log()));
        StepVerifier.create(logUseCase.handlerLog(alert, "SMS", response,true))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void putLogTestWithFalse() {
        StepVerifier.create(logUseCase.handlerLog(alert, "", response,false))
                .expectNextCount(1)
                .verifyComplete();
    }

}
