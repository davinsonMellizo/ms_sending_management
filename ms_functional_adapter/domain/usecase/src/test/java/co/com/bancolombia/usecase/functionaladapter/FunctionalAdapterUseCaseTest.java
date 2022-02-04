package co.com.bancolombia.usecase.functionaladapter;

import co.com.bancolombia.model.message.Message;
import co.com.bancolombia.model.transaction.Transaction;
import co.com.bancolombia.model.transaction.gateways.TransactionGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class FunctionalAdapterUseCaseTest {
    @Mock
    private TransactionGateway transactionGateway;
    @InjectMocks
    private FunctionalAdapterUseCase useCase;

    @BeforeEach
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void handleSendAlert() {
        when(transactionGateway.sendTransactionToMQ(any())).thenReturn(Mono.just(new Message()));
        StepVerifier.create(useCase.sendTransactionToMQ(new Transaction()))
                .verifyComplete();
    }
}
