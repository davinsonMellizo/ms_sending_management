package co.com.bancolombia.usecase.log;

import co.com.bancolombia.model.log.Log;
import co.com.bancolombia.model.log.gateways.LogGateway;
import co.com.bancolombia.model.message.Message;
import co.com.bancolombia.model.message.Response;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class LogUseCase {
    private final LogGateway logGateway;

    public <T> Mono<T> sendLog(Message message, String logType, String medium, Response response) {
        return logGateway.putLogToSQS(Log.builder()
                .documentType(message.getDocumentType())
                .documentNumber(Long.parseLong(message.getDocumentNumber()))
                //.consumer(message.getConsumer())
                .logType(logType)
                .medium(medium)
                .contact(message.getTo())
                .messageSent(message.getText())
                //.alertId(alert.getId())
                //.alertDescription(alert.getDescription())
                //.transactionId(message.getTransactionCode())
                //.amount(message.getAmount())
                .provider(message.getProvider())
                .responseCode(response.getCode())
                .responseDescription(response.getDescription())
                //.operationId(message.getOperation())
                .build())
                .then(Mono.empty());
    }
}
