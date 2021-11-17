package co.com.bancolombia.usecase.sendalert;

import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.message.Message;
import co.com.bancolombia.model.message.PUSH;
import co.com.bancolombia.model.message.Response;
import co.com.bancolombia.model.message.gateways.PushGateway;
import co.com.bancolombia.usecase.log.LogUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.constants.TypeLogSend.SEND_220;
import static co.com.bancolombia.commons.constants.TypeLogSend.SEND_230;

@RequiredArgsConstructor
public class RouterProviderPushUseCase {
    private final LogUseCase logUseCase;
    private final PushGateway pushGateway;


    public Mono<Response> sendPush(Message message, Alert alert) {
        return logUseCase.sendLogPush(message, alert, SEND_220, alert.getMessage(), new Response(0, "Success"))
                .map(response -> PUSH.builder()
                .data(PUSH.Data.builder()
                        .sendMessage(PUSH.SendMessage.builder()
                                .customerIdentification(PUSH.CustomerIdentification.builder()
                                        .customerDocumentNumber(Long.toString(message.getDocumentNumber()))
                                        .customerDocumentType(Integer.toString(message.getDocumentType()))
                                        .build())
                                .consumerId("AYN")
                                .message(alert.getMessage())
                                .build())
                        .build())
                .build())
                .flatMap(pushGateway::sendPush)
                .doOnError(e -> Response.builder().code(1).description(e.getMessage()).build())
                .flatMap(response -> logUseCase.sendLogPush(message, alert, SEND_230,
                        alert.getMessage(), response));
    }
}
