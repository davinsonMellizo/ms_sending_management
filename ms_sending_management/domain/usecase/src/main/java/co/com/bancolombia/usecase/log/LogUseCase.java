package co.com.bancolombia.usecase.log;

import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.events.gateways.CommandGatewayLog;
import co.com.bancolombia.model.log.Log;
import co.com.bancolombia.model.message.Message;
import co.com.bancolombia.model.message.Response;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class LogUseCase {
    private final CommandGatewayLog commandGateway;

    public <T> Mono<T> sendLogSMS(Message message, Alert alert, String logType, Response response) {
        return commandGateway.sendCommandLogAlert(Log.builder()
                .logKey(message.getLogKey())
                .documentType(message.getDocumentType())
                .documentNumber(message.getDocumentNumber())
                .consumer(message.getConsumer())
                .logType(logType)
                .medium("SMS")
                .contact(message.getPhone())
                .messageSent(alert.getMessage())
                .alertId(alert.getId())
                .template(alert.getTemplateName())
                .alertDescription(alert.getDescription())
                .transactionId(message.getTransactionCode())
                .amount(message.getAmount())
                .responseCode(response.getCode())
                .responseDescription(response.getDescription())
                .operationId(message.getOperation())
                .priority(alert.getPriority())
                .build())
                .then(Mono.empty());
    }

    public Mono<Response> sendLogPush(Message message, Alert alert, String logType, Response response) {
        return commandGateway.sendCommandLogAlert(Log.builder()
                .logKey(message.getLogKey())
                .documentType(message.getDocumentType())
                .documentNumber(message.getDocumentNumber())
                .consumer(message.getConsumer())
                .logType(logType)
                .medium("PUSH")
                .template(message.getTemplate())
                .contact(message.getPhone())
                .messageSent(alert.getMessage())
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

    public <T> Mono<T>  sendLogMAIL(Message message, Alert alert, String logType, Response response) {
        return commandGateway.sendCommandLogAlert(Log.builder()
                .logKey(message.getLogKey())
                .documentType(message.getDocumentType())
                .documentNumber(message.getDocumentNumber())
                .consumer(message.getConsumer())
                .logType(logType)
                .medium("MAIL")
                .template(alert.getTemplateName())
                .contact(message.getMail())
                .messageSent(alert.getMessage())
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
        return commandGateway.sendCommandLogAlert(Log.builder()
                .logKey(message.getLogKey())
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
