package co.com.bancolombia.ibmmq;

import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.ibmmq.exceptions.JMSExtException;
import co.com.bancolombia.ibmmq.jms.JmsManagement;
import co.com.bancolombia.ibmmq.model.QueueDto;
import co.com.bancolombia.model.log.LoggerBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.JMSRuntimeException;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.TECHNICAL_JMS_ERROR;


@Component
@RequiredArgsConstructor
public class MqConnector implements IConnector<String> {

    private final JmsManagement connManagment;
    private final LoggerBuilder loggerBuilder;

    @Override
    public Mono<String> sendMessageToQueue(String message, String keyConnect, String correlationID) {
        QueueDto queueRequest = connManagment.getConnectionData().getQueueFromTransaction(keyConnect);
        return Mono.defer(() -> {
            try {
                QueueDto queueResponse = connManagment.getConnectionData().getQueueResponseFromTransaction(keyConnect);
                TextMessage txtMessage = connManagment.getTextMessage(queueRequest.getConnection(), message);
                txtMessage.setJMSCorrelationID(correlationID);
                txtMessage.setJMSReplyTo(connManagment.getQueue(queueResponse));
                JMSProducer msgP = connManagment.getJmsProducer(queueRequest.getConnection());
                msgP.send(connManagment.getQueue(queueRequest), txtMessage);
                return Mono.just(txtMessage.getJMSMessageID());
            } catch (JMSException e) {
                return Mono.error(e);
            }
        })
                .doOnError(ex -> ex instanceof JMSRuntimeException || ex instanceof JMSException,
                        e -> handleReconnect(e, queueRequest.getConnection()))
                .onErrorMap(error -> new TechnicalException(error, TECHNICAL_JMS_ERROR))
                .subscribeOn(Schedulers.elastic());
    }

    @Override
    public void eventConsumer(QueueDto queue, MessageListener msgListener) {
        connManagment.setMessageListener(queue, msgListener);
    }

    private void handleReconnect(Throwable e, String connectionName) {
        loggerBuilder.info("Handling exception to ExceptionListener for retry");
        connManagment.getExceptionListener().onException(new JMSExtException(e.getMessage(), connectionName));
    }
}