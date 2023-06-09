package co.com.bancolombia.usecase.sendalert.routers;

import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.alert.Alert;
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
import java.util.function.Predicate;

import static co.com.bancolombia.commons.constants.Constants.NOT_SENT;
import static co.com.bancolombia.commons.constants.Constants.PUSH_BY_SMS;
import static co.com.bancolombia.commons.constants.Constants.SI;
import static co.com.bancolombia.commons.constants.Constants.SMS_WAS_NOT_SENT;
import static co.com.bancolombia.commons.constants.Constants.SUCCESS;
import static co.com.bancolombia.commons.constants.Medium.PUSH;
import static co.com.bancolombia.commons.constants.Medium.SMS;
import static co.com.bancolombia.commons.constants.TypeLogSend.SEND_220;
import static co.com.bancolombia.commons.constants.TypeLogSend.SEND_230;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.ALERT_HAS_NO_PUSH;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.APPLICATION_CODE_REQUIRED;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.CLIENT_HAS_NO_PUSH;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.CLIENT_IDENTIFICATION_INVALID;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.INVALID_MESSAGE;

@RequiredArgsConstructor
public class RouterProviderPushUseCase {
    private final LogUseCase logUseCase;
    private final PushGateway pushGateway;
    private final RouterProviderSMSUseCase routerProviderSMSUseCase;

    private static final Predicate<Message> validatePreferences = message ->
            (message.getRetrieveInformation().equals(Boolean.TRUE) &&
                    (message.getPreferences().contains(PUSH) || message.getPreferences().isEmpty())) ||
                    (message.getRetrieveInformation().equals(Boolean.FALSE) && !message.getApplicationCode().isEmpty());
    private static final Predicate<Message> validatePreferencesWithoutEmpty = message ->
            (message.getRetrieveInformation().equals(Boolean.TRUE) && message.getPreferences().contains(PUSH)) ||
                    (message.getRetrieveInformation().equals(Boolean.FALSE) && !message.getApplicationCode().isEmpty());

    public Mono<Response> sendPush(Message message, Alert alert, Response response) {
        Map<String, String> headers = new HashMap<>();
        headers.put("message-id", message.getLogKey());
        message.setPush(true);

        return logUseCase.sendLogPush(message, alert, SEND_220, response)
                .map(message1 -> Push.builder()
                        .applicationCode(message.getApplicationCode())
                        .categoryId(alert.getIdCategory().toString())
                        .consumerId("AYN")
                        .customerDocumentNumber(message.getDocumentNumber().toString())
                        .customerDocumentType(message.getDocumentType().toString())
                        .message(alert.getMessage())
                        .build())
                .doOnNext(push -> push.setHeaders(headers))
                .flatMap(pushGateway::sendPush)
                .onErrorResume(e -> Mono.just(Response.builder().code(1).description(e.toString()).build()))
                .flatMap(responsePush -> logUseCase.sendLogPush(message, alert, SEND_230, responsePush))
                .thenReturn(response);
    }

    private Mono<Response> sendContingencySms(Message message, Alert alert, Response response) {
        return Mono.just(message)
                .filter(validatePreferencesWithoutEmpty)
                .flatMap(men -> logUseCase.sendLogPush(message, alert, SEND_220, response))
                .switchIfEmpty(Mono.just(response))
                .filter(res -> !message.getPreferences().contains(SMS) && message.getRetrieveInformation())
                .filter(message1 -> !alert.getMessage().isEmpty())
                .flatMap(res -> routerProviderSMSUseCase.sendAlertToSMS(alert, message))
                .map(res -> response.toBuilder().description(response.getDescription() + PUSH_BY_SMS).build())
                .switchIfEmpty(Mono.just(response))
                .onErrorResume(e -> Mono.just(response.toBuilder()
                        .description(response.getDescription() + SMS_WAS_NOT_SENT + e.getMessage()).build()));
    }

    private Mono<Message> validateClient(Message pMessage) {
        return Mono.just(pMessage)
                .filter(message1 -> Objects.nonNull(pMessage.getDocumentNumber()))
                .filter(message1 -> Objects.nonNull(pMessage.getDocumentType()))
                .switchIfEmpty(Mono.error(new BusinessException(CLIENT_IDENTIFICATION_INVALID)));
    }

    private Mono<Message> validateClientAndAlertHavePush(Message pMessage, Alert alert) {
        return Mono.just(pMessage)
                .filter(Message::getPush)
                .switchIfEmpty(Mono.error(new BusinessException(CLIENT_HAS_NO_PUSH)))
                .filter(message -> alert.getPush().equals(SI))
                .switchIfEmpty(Mono.error(new BusinessException(ALERT_HAS_NO_PUSH)));
    }

    private Mono<Message> validateSendPush(Message pMessage, Alert alert) {
        return Mono.just(pMessage)
                .filter(message -> !message.getApplicationCode().isEmpty())
                .switchIfEmpty(Mono.error(new BusinessException(APPLICATION_CODE_REQUIRED)))
                .filter(message1 -> !alert.getMessage().isEmpty())
                .switchIfEmpty(Mono.error(new BusinessException(INVALID_MESSAGE)))
                .filter(Message::getRetrieveInformation)
                .flatMap(message -> validateClientAndAlertHavePush(pMessage, alert))
                .switchIfEmpty(validateClient(pMessage));
    }

    public Mono<Response> routeAlertPush(Message pMessage, Alert alert) {
        return Mono.just(pMessage)
                .filter(validatePreferences)
                .flatMap(message -> validateSendPush(pMessage, alert))
                .map(message -> new Response(0, SUCCESS, PUSH, alert.getId()))
                .flatMap(response -> sendPush(pMessage, alert, response))
                .onErrorResume(e -> sendContingencySms(pMessage, alert,
                        new Response(1, e.getMessage(), PUSH, alert.getId())))
                .switchIfEmpty(Mono.just(new Response(1, NOT_SENT, PUSH, alert.getId())));
    }
}
