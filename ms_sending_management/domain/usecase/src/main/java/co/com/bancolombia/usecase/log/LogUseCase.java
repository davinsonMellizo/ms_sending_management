package co.com.bancolombia.usecase.log;

import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.log.Log;
import co.com.bancolombia.model.log.gateways.LogGateway;
import co.com.bancolombia.model.message.Message;
import co.com.bancolombia.model.message.Response;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class LogUseCase {
    private final LogGateway logGateway;

    public <T> Mono<T> sendLogSMS(Message message, Alert alert, String logType, String text, Response response) {
        return logGateway.putLogToSQS(Log.builder()
                .documentType(message.getDocumentType())
                .documentNumber(message.getDocumentNumber())
                .consumer(message.getConsumer())
                .logType(logType)
                .medium("SMS")
                .template(message.getTemplate())
                .contact(message.getPhone())
                .messageSent(text)
                .alertId(alert.getId())
                .alertDescription(alert.getDescription())
                .transactionId(message.getTransactionCode())
                .amount(message.getAmount())
                .responseCode(response.getCode())
                .responseDescription(response.getDescription())
                .operationId(message.getOperation())
                .build())
                .then(Mono.empty());
    }

    public Mono<Response> sendLogPush(Message message, Alert alert, String logType, String text, Response response) {
        return logGateway.putLogToSQS(Log.builder()
                .documentType(message.getDocumentType())
                .documentNumber(message.getDocumentNumber())
                .consumer(message.getConsumer())
                .logType(logType)
                .medium("PUSH")
                .template(message.getTemplate())
                .contact(message.getPhone())
                .messageSent(text)
                .alertId(alert.getId())
                .alertDescription(alert.getDescription())
                .transactionId(message.getTransactionCode())
                .amount(message.getAmount())
                .responseCode(response.getCode())
                .responseDescription(response.getDescription())
                .operationId(message.getOperation())
                .build())
                .then(Mono.just(response));
    }

    public <T> Mono<T> sendLogMAIL(Message message, Alert alert, String logType, String text, Response response) {
        return logGateway.putLogToSQS(Log.builder()
                .documentType(message.getDocumentType())
                .documentNumber(message.getDocumentNumber())
                .consumer(message.getConsumer())
                .logType(logType)
                .medium("MAIL")
                .template(message.getTemplate())
                .contact(message.getMail())
                .messageSent(text)
                .alertId(alert.getId())
                .alertDescription(alert.getDescription())
                .transactionId(message.getTransactionCode())
                .amount(message.getAmount())
                .responseCode(response.getCode())
                .responseDescription(response.getDescription())
                .operationId(message.getOperation())
                .build())
                .then(Mono.empty());
    }

    public <T> Mono<T> sendLogError(Message message, String logType, Response response) {
        return logGateway.putLogToSQS(Log.builder()
                .documentType(message.getDocumentType())
                .documentNumber(message.getDocumentNumber())
                .consumer(message.getConsumer())
                .logType(logType)
                .responseCode(response.getCode())
                .responseDescription(response.getDescription())
                .operationId(message.getOperation())
                .build())
                .then(Mono.empty());
    }
}
