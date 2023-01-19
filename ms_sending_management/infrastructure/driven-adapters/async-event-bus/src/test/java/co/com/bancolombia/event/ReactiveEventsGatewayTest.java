package co.com.bancolombia.event;

import co.com.bancolombia.events.ReactiveDirectAsyncGateway;
import co.com.bancolombia.model.log.LoggerBuilder;
import co.com.bancolombia.model.message.Mail;
import co.com.bancolombia.model.message.Sms;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.async.api.DirectAsyncGateway;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReactiveEventsGatewayTest {

    @InjectMocks
    private ReactiveDirectAsyncGateway reactiveDirectAsyncGateway;
    
    @Mock
    private DirectAsyncGateway directAsyncGateway;

    @Mock
    private LoggerBuilder loggerBuilder;

    @BeforeEach
    public void init() {
        when(directAsyncGateway.sendCommand(any(), anyString())).thenReturn(Mono.empty());
    }

    @Test
    void sendEventSMSTest() {
        StepVerifier.create(reactiveDirectAsyncGateway.sendCommandAlertSms(new Sms())).verifyComplete();
    }

    @Test
    void sendEventEMAILTest() {
        StepVerifier.create(reactiveDirectAsyncGateway.sendCommandAlertEmail(new Mail())).verifyComplete();
    }



}
