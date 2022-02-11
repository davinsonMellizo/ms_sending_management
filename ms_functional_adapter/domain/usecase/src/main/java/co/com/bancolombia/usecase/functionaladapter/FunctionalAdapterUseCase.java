package co.com.bancolombia.usecase.functionaladapter;

import co.com.bancolombia.model.transaction.Transaction;
import co.com.bancolombia.model.transaction.gateways.TransactionGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class FunctionalAdapterUseCase {
    private final TransactionGateway transactionGateway;

    public Mono<Void> sendTransactionToMQ(Transaction transaction){
        return transactionGateway.sendTransactionToMQ(transaction)
                .thenEmpty(Mono.empty());
    }

}
