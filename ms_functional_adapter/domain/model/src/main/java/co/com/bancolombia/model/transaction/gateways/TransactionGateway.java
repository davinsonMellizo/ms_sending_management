package co.com.bancolombia.model.transaction.gateways;
import co.com.bancolombia.model.message.Message;
import co.com.bancolombia.model.transaction.Transaction;
import reactor.core.publisher.Mono;

public interface TransactionGateway {
    Mono<Message> sendTransactionToMQ(Transaction transaction);
}