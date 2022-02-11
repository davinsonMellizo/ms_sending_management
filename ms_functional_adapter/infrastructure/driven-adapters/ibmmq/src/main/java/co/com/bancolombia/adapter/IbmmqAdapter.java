package co.com.bancolombia.adapter;

import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.commons.freemarker.CommonTemplate;
import co.com.bancolombia.ibmmq.IConnector;
import co.com.bancolombia.model.log.LoggerBuilder;
import co.com.bancolombia.model.message.Message;
import co.com.bancolombia.model.transaction.Transaction;
import co.com.bancolombia.model.transaction.gateways.TransactionGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.TECHNICAL_JMS_ERROR;

@Component
@RequiredArgsConstructor
public class IbmmqAdapter implements TransactionGateway {

    private final IConnector<String> connector;
    private final LoggerBuilder loggerBuilder;

    @Override
    public Mono<Message> sendTransactionToMQ(Transaction transaction) {
        return buildDataFromTemplate(transaction)
                .doOnSuccess(trama -> loggerBuilder.info(trama,
                        transaction.getMessageId(),
                        transaction.getChannel(),
                        transaction.getNroTransaction()))
                .flatMap(con -> connector.sendMessageToQueue(con, this.keyConnection(transaction),
                        transaction.getCorrelationID()))
                .map(this::buildMessage)
                .onErrorMap(error -> {
                    loggerBuilder.error(error, transaction.getMessageId(), transaction.getChannel(),
                            transaction.getNroTransaction());
                    return new TechnicalException(error, TECHNICAL_JMS_ERROR);
                });
    }

    private Mono<String> buildDataFromTemplate(Transaction transaction){
        return CommonTemplate.create(transaction.getTemplate())
                .flatMap(tpl -> tpl.process(transaction.getPayload()))
                .doOnError(throwable ->loggerBuilder.info(throwable.getMessage()))
                .onErrorResume(throwable ->  Mono.empty());
    }

    private Message buildMessage(String textMessage) {
            return Message.builder()
                    .messageId(textMessage)
                    .build();
    }

    private String keyConnection(Transaction transaction){
        return transaction.getChannel().concat(transaction.getNroTransaction());
    }
}
