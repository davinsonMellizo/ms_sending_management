package co.com.bancolombia.model.transaction.gateways;

import co.com.bancolombia.model.transaction.Transaction;
import reactor.core.publisher.Mono;

public interface CommandGateway {
    Mono<Void> sendTransaction(Transaction transaction);
}
