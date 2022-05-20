package co.com.bancolombia.usecase.functionaladapter;

import co.com.bancolombia.model.transaction.Transaction;
import co.com.bancolombia.model.transaction.gateways.CommandGateway;
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

class FunctionalAdapterUseCaseTest {
    @Mock
    private TransactionGateway transactionGateway;
    @Mock
    private CommandGateway commandGateway;
    @InjectMocks
    private FunctionalAdapterUseCase useCase;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void handleSendAlert() {
        when(transactionGateway.sendTransactionToMQ(any())).thenReturn(Mono.empty());
        StepVerifier.create(useCase.sendTransactionToMQ(new Transaction()))
                .verifyComplete();
    }

    @Test
    void handleSendAlertToRabbit() {
        when(commandGateway.sendTransaction(any())).thenReturn(Mono.empty());
        StepVerifier.create(useCase.sendTransactionToRabbit(new Transaction()))
                .verifyComplete();
    }
}
