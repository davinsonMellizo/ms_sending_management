package co.com.bancolombia.usecase.sendalert;

import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.message.Message;
import co.com.bancolombia.model.message.PUSH;
import co.com.bancolombia.model.message.Response;
import co.com.bancolombia.usecase.log.LogUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.constants.TypeLogSend.SEND_230;

@RequiredArgsConstructor
public class RouterProviderPushUseCase {
    private final LogUseCase logUseCase;


    public Mono<Response> sendPush(Message message, Alert alert) {
        return Mono.just(PUSH.builder()
                .data(PUSH.Data.builder()
                        .sendMessage(PUSH.SendMessage.builder()
                                .customerIdentification(PUSH.CustomerIdentification.builder()
                                        .customerDocumentNumber(Long.toString(message.getDocumentNumber()))
                                        .customerDocumentType(Integer.toString(message.getDocumentType()))
                                        .build())
                                .consumerId("AYN")
                                .message(message.getParameters().get(0).getValue())
                                .build())
                        .build())
                .build())
                .map(mail -> Response.builder().code(200).build())//TODO call api PUSH;
                .doOnError(e -> Response.builder().code(1).description(e.getMessage()).build())
                .flatMap(response -> logUseCase.sendLogSMS(message, alert, SEND_230,
                        message.getParameters().get(0).getValue(), response));
    }
}
