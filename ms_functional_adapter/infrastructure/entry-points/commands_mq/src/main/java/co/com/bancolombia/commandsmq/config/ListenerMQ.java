package co.com.bancolombia.commandsmq.config;

import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.model.log.LoggerBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import static com.ibm.msg.client.jms.JmsConstants.JMSX_DELIVERY_COUNT;

@Primary
@Component
@RequiredArgsConstructor
public class ListenerMQ /*implements MessageListener*/ {

    /*private final FunctionalAdapterUseCase useCase;
    private final LoggerBuilder loggerBuilder;
    private final Environment env;
    private static final String MAX_RETRIES = "app.async.maxRetries";

    @Override
    public void onMessage(Message message) {
        getMessage(message)
                //.flatMap(useCase::sendTransactionResponse)
                //.onErrorResume(e -> discard(e, message))
                .subscribe();
    }

    private Mono<Transaction> getMessage(Message message){
        try {
            return Mono.just(Transaction.builder()
                    .payload(message.getBody(String.class))
                    .correlationID(message.getJMSCorrelationID())
                    .build());
        } catch (JMSException e) {
            return Mono.error(e);
        }
        return Mono.empty();
    }

    private Mono<Void> discard(Throwable error, Message message) {
        final boolean isRetry = false;//error instanceof TechnicalException && ((TechnicalException) error).getException().isRetry();
        final int maxRetries = env.getProperty(MAX_RETRIES, Integer.class);
        return Mono.defer(() -> {
            try {
                if (message.getIntProperty(JMSX_DELIVERY_COUNT) > maxRetries  || isRetry) {
                    return Mono.empty();
                }
            } catch (JMSException e) {
                loggerBuilder.error(e);
            }
            return Mono.error(error);
        });
    }*/
}
