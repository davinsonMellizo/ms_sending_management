package co.com.bancolombia.usecase.log;

import co.com.bancolombia.model.log.Log;
import co.com.bancolombia.model.log.gateways.LogGateway;
import co.com.bancolombia.model.message.Alert;
import co.com.bancolombia.model.message.Response;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class LogUseCase {
    private final LogGateway logGateway;

    public <T> Mono<T> sendLog(Alert alert, String logType, String medium, Response response) {
        return logGateway.putLogToSQS(Log.builder()
                .documentType(alert.getDocumentType())
                .documentNumber(Long.parseLong(alert.getDocumentNumber()))
                //.consumer(message.getConsumer())
                .logType(logType)
                .medium(medium)
                .contact(alert.getTo())
                .messageSent(alert.getText())
                //.alertId(alert.getId())
                //.alertDescription(alert.getDescription())
                //.transactionId(message.getTransactionCode())
                //.amount(message.getAmount())
                .provider(alert.getProvider())
                .responseCode(response.getCode())
                .responseDescription(response.getDescription())
                //.operationId(message.getOperation())
                .build())
                .then(Mono.empty());
    }
}
