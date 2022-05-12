package co.com.bancolombia.event;

import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.commons.utils.JsonUtils;
import co.com.bancolombia.events.ReactiveDirectAsyncGateway;
import co.com.bancolombia.ibmmq.jms.JmsManagement;
import co.com.bancolombia.ibmmq.model.ConnectionData;
import co.com.bancolombia.model.datatest.FunctionalAdapterDataTest;
import co.com.bancolombia.model.log.LoggerBuilder;
import co.com.bancolombia.model.transaction.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.async.api.DirectAsyncGateway;
import org.reactivecommons.async.api.From;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReactiveEventsGatewayTest {

    @InjectMocks
    private ReactiveDirectAsyncGateway reactiveDirectAsyncGateway;
    @Mock
    private DirectAsyncGateway directAsyncGateway;
    @Mock
    private JmsManagement connMgm;
    @Mock
    private ConnectionData connectionDataMock;
    @Mock
    private LoggerBuilder loggerBuilder;

    private Transaction transaction;
    private co.com.bancolombia.ibmmq.model.Transaction transactionConn;
    private static final String template = "{ \"name\" : \"prueba\"}";
    private static final String transactionString = "{\"listener\" : \"id2\",\"template\" : \"message to send\",\"target\": \"ms_contact_management\",\"queue\": \"send.update.client\" }";

    @BeforeEach
    public void init(){
        transaction = FunctionalAdapterDataTest.buildTransaction();
        transactionConn = JsonUtils.stringToType(transactionString, co.com.bancolombia.ibmmq.model.Transaction.class);
        when(connectionDataMock.getTransaction(any(String.class))).thenReturn(Mono.just(transactionConn));
        when(connectionDataMock.getTemplate(any(String.class))).thenReturn(Mono.just(template));
        when(connMgm.getConnectionData()).thenReturn(connectionDataMock);

    }

    @Test
    public void sendEventTest(){

        when(directAsyncGateway.sendCommand(any(), anyString())).thenReturn(Mono.empty());
        StepVerifier.create(reactiveDirectAsyncGateway.sendTransaction(transaction)).verifyComplete();
    }

    @Test
    public void givenErrorWhenSendEventTest(){
        when(directAsyncGateway.sendCommand(any(), anyString()))
                .thenThrow(RuntimeException.class);
        StepVerifier.create(reactiveDirectAsyncGateway.sendTransaction(transaction))
                .expectError(TechnicalException.class).verify();
    }
}
