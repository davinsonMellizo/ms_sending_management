package co.com.bancolombia.event;

import co.com.bancolombia.events.AsyncGatewayLog;
import co.com.bancolombia.model.log.Log;
import co.com.bancolombia.model.log.LoggerBuilder;
import co.com.bancolombia.rabbitmq.config.dual.sender.DirectAsyncDualGateway;
import org.junit.jupiter.api.BeforeEach;
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
class AsyncGatewayLogTest {

    @InjectMocks
    private AsyncGatewayLog asyncGatewayLogTest;

    @Mock
    private DirectAsyncDualGateway directAsyncGateway;

    @Mock
    private LoggerBuilder loggerBuilder;

    @BeforeEach
    public void init() {
        when(directAsyncGateway.sendCommand(any(), anyString())).thenReturn(Mono.empty());
    }

    @Test
    void sendEventLogTest() {
        StepVerifier.create(asyncGatewayLogTest.sendCommandLogAlert(new Log())).verifyComplete();
    }


}
