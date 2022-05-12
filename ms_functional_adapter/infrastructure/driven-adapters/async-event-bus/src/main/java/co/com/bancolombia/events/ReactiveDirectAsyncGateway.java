package co.com.bancolombia.events;

import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.commons.freemarker.CommonTemplate;
import co.com.bancolombia.commons.utils.JsonUtils;
import co.com.bancolombia.ibmmq.jms.JmsManagement;
import co.com.bancolombia.model.log.LoggerBuilder;
import co.com.bancolombia.model.transaction.Transaction;
import co.com.bancolombia.model.transaction.gateways.CommandGateway;
import lombok.AllArgsConstructor;
import org.reactivecommons.api.domain.Command;
import org.reactivecommons.async.api.DirectAsyncGateway;
import org.reactivecommons.async.impl.config.annotations.EnableDirectAsyncGateway;
import org.springframework.cglib.core.Local;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;


import static reactor.core.publisher.Mono.from;
import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.TECHNICAL_EVENT_EXCEPTION;

@AllArgsConstructor
@EnableDirectAsyncGateway
public class ReactiveDirectAsyncGateway implements CommandGateway {
    private final DirectAsyncGateway gateway;
    private final JmsManagement connMgm;
    private static final String PAYLOAD_TAG = "payload";
    private final LoggerBuilder loggerBuilder;


    @Override
    public Mono<Void> sendTransaction(Transaction transaction) {
        System.out.println("llega el comando con data sincrono: "+transaction.getPayload()+" ***");
        return buildDataFromTemplate(transaction)
                .map( data -> (Object)data)
                .flatMap(data -> sendCommand(data, transaction))
                .doOnSuccess(r -> sendLog(null, transaction))
                .onErrorMap(error -> new TechnicalException(error, TECHNICAL_EVENT_EXCEPTION))
                .doOnError(error -> sendLog(error, transaction))
                .then();
    }

    private Mono<String> buildDataFromTemplate(Transaction trx){
        return getTemplate(trx)
                .flatMap(CommonTemplate::create)
                .flatMap(tpl -> tpl.process(Map.of(PAYLOAD_TAG, trx.getPayload())))
                .doOnError(throwable ->loggerBuilder.info(throwable.getMessage()))
                .onErrorResume(throwable ->  Mono.empty());
    }

    private Mono<Void> sendCommand(Object data, Transaction trx) {
        return getTransaction(trx)
                .doOnNext(transaction -> System.out.println("Target: "+transaction.getTarget()))
                .doOnNext(transaction -> System.out.println("Queue: "+transaction.getQueue()))
                .zipWhen(transaction -> Mono.just(new Command<>(transaction.getQueue(), UUID.randomUUID().toString(), data)))
                .flatMap(commandTuple -> gateway.sendCommand(commandTuple.getT2(), commandTuple.getT1().getTarget()))
                .thenReturn(data.toString() + LocalDateTime.now())
                .doOnNext(System.out::println)
                .then(Mono.empty());
    }

    private Mono<String> getTemplate(Transaction trx){
        return connMgm.getConnectionData()
                .getTemplate(((String)trx.getPayload()).substring(0,11));
    }
    private Mono<co.com.bancolombia.ibmmq.model.Transaction> getTransaction(Transaction trx){
        return Mono.just(((String)trx.getPayload()).substring(0,11))
                .flatMap(template -> connMgm.getConnectionData().getTransaction(template));
    }

    private void sendLog(Throwable error, Transaction transaction ){
        if(Objects.nonNull(error)){
            loggerBuilder.error(error, transaction.getMessageId(),
                    transaction.getChannel(),
                    transaction.getNroTransaction());
        }else{
            loggerBuilder.info(transaction.getPayload(),
                    transaction.getMessageId(),
                    transaction.getChannel(),
                    transaction.getNroTransaction());
        }
    }

}
