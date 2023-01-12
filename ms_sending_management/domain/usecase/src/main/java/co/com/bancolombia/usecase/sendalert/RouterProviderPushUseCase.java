package co.com.bancolombia.usecase.sendalert;

import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.document.gateways.DocumentGateway;
import co.com.bancolombia.model.message.Message;
import co.com.bancolombia.model.message.Push;
import co.com.bancolombia.model.message.Response;
import co.com.bancolombia.model.message.gateways.PushGateway;
import co.com.bancolombia.usecase.log.LogUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

import static co.com.bancolombia.commons.constants.TypeLogSend.SEND_220;
import static co.com.bancolombia.commons.constants.TypeLogSend.SEND_230;

@RequiredArgsConstructor
public class RouterProviderPushUseCase {
    private final LogUseCase logUseCase;
    private final PushGateway pushGateway;
    private final DocumentGateway documentGateway;

    public Mono<Response> sendPush(Message message, Alert alert) {
        Map<String, String> headers = new HashMap<>();
        headers.put("message-id",message.getLogKey());
        return logUseCase.sendLogPush(message, alert, SEND_220, new Response(0, "Success"))
                .flatMap(response -> documentGateway.getDocument(message.getDocumentType().toString()))
                .map(document -> Push.builder()
                        .applicationCode("PERSONAS")
                        .categoryId(alert.getIdCategory().toString())
                        .consumerId("AYN")
                        .customerDocumentNumber(message.getDocumentNumber().toString())
                        .customerDocumentType(document.getCode())
                        .message(alert.getMessage())
                        .build())
                .doOnNext(push -> push.setHeaders(headers))
                .flatMap(pushGateway::sendPush)
                .doOnError(e -> Response.builder().code(1).description(e.getMessage()).build())
                .flatMap(response -> logUseCase.sendLogPush(message, alert, SEND_230, response));
    }
}
