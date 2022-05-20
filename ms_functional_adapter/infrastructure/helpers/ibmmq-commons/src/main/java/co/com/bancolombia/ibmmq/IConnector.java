package co.com.bancolombia.ibmmq;

import co.com.bancolombia.ibmmq.model.QueueDto;
import reactor.core.publisher.Mono;

import javax.jms.MessageListener;

public interface IConnector<T> {

    Mono<T> sendMessageToQueue(String message, String keyConnect, String correlationID);

    void eventConsumer(QueueDto queue, MessageListener msgListener);
}
