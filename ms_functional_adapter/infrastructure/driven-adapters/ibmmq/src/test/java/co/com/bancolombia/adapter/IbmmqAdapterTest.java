package co.com.bancolombia.adapter;

import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.ibmmq.IConnector;
import co.com.bancolombia.model.datatest.FunctionalAdapterDataTest;
import co.com.bancolombia.model.log.LoggerBuilder;
import co.com.bancolombia.model.transaction.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class IbmmqAdapterTest {
    
    @InjectMocks
    private IbmmqAdapter ibmmqAdapter;

    @Mock
    private IConnector<String> connector;

    @Mock
    private LoggerBuilder loggerBuilder;

    private Transaction transaction;
    private static final String textMessage = "123456";

    @BeforeEach
    public void init(){
        transaction = FunctionalAdapterDataTest.buildTransaction();
    }

    @Test
    public void shouldSendTransactionToMQTest(){
        when(connector.sendMessageToQueue(any(String.class), any(String.class), any(String.class)))
                .thenReturn(Mono.just(textMessage));
        StepVerifier.create(ibmmqAdapter.sendTransactionToMQ(transaction))
                .expectNextMatches(msg -> msg.getMessageId().equalsIgnoreCase(textMessage))
                .verifyComplete();
    }

    @Test
    public void givenExceptionWhenSendTransactionToMQTest(){
        when(connector.sendMessageToQueue(any(), any(), any())).thenThrow(RuntimeException.class);
        ibmmqAdapter.sendTransactionToMQ(transaction).as(StepVerifier::create)
                .expectError(TechnicalException.class).verify();
    }


}
