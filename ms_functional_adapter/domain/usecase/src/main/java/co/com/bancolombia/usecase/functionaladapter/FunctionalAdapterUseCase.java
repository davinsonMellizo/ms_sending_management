package co.com.bancolombia.usecase.functionaladapter;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class FunctionalAdapterUseCase {
    //private final TransactionGateway transactionGateway;

    public Mono<Void> sendTransactionToMQ(String s){
        System.out.println(s);
        return Mono.error(new Throwable());
    }

}
