package co.com.bancolombia.usecase.sendalert.routers;

import co.com.bancolombia.commons.exceptions.BusinessException;
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
import java.util.Objects;

import static co.com.bancolombia.commons.constants.Constants.PUSH_SMS;
import static co.com.bancolombia.commons.constants.Constants.SI;
import static co.com.bancolombia.commons.constants.TypeLogSend.SEND_220;
import static co.com.bancolombia.commons.constants.TypeLogSend.SEND_230;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.CLIENT_IDENTIFICATION_INVALID;

@RequiredArgsConstructor
public class RouterProviderPushUseCase {
    private final LogUseCase logUseCase;
    private final PushGateway pushGateway;
    private final DocumentGateway documentGateway;

    public Mono<Response> sendPush(Message message, Alert alert) {
        Map<String, String> headers = new HashMap<>();
        headers.put("message-id",message.getLogKey());
        message.setPush(true);
        return Mono.just(message)
                .filter(message1 -> message1.getPreferences().contains("SMS") || message1.getPreferences().isEmpty())
                .filter(message1 -> alert.getPush().equals(SI) || alert.getPush().equals(PUSH_SMS))
                .flatMap(this::validatePush)
                .flatMap(this::validateClient)
                .filter(Message::getPush)
                .doOnNext(message1 -> logUseCase.sendLogPush(message, alert, SEND_220, new Response(0, "Success")))
                .flatMap(response -> documentGateway.getDocument(message.getDocumentType().toString()))
                .map(document -> Push.builder()
                        .applicationCode(message.getApplicationCode())
                        .categoryId(alert.getIdCategory().toString())
                        .consumerId("AYN")
                        .customerDocumentNumber(message.getDocumentNumber().toString())
                        .customerDocumentType(document.getCode())
                        .message(alert.getMessage())
                        .build())
                .doOnNext(push -> push.setHeaders(headers))
                .flatMap(pushGateway::sendPush)
                .onErrorResume(e -> Mono.just(Response.builder().code(1).description(e.toString()).build()))
                .flatMap(response -> logUseCase.sendLogPush(message, alert, SEND_230, response))
                .switchIfEmpty(Mono.just(new Response()));
    }

    private Mono<Message> validatePush(Message message){
        return Mono.just(message)
                .filter(Message::getRetrieveInformation)
                .switchIfEmpty(Mono.just(message.toBuilder()
                        .push(!message.getApplicationCode().isEmpty())
                        .build()));
    }

    private Mono<Message> validateClient(Message message){
        return Mono.just(message)
                .filter(message1 -> Objects.nonNull(message.getDocumentNumber()))
                .filter(message1 -> Objects.nonNull(message.getDocumentType()))
                .switchIfEmpty(Mono.error(new BusinessException(CLIENT_IDENTIFICATION_INVALID)));
    }
}
