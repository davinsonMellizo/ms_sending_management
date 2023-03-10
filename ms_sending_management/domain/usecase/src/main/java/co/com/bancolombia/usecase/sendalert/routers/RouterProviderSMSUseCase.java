package co.com.bancolombia.usecase.sendalert.routers;


import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.events.gateways.CommandGateway;
import co.com.bancolombia.model.message.Message;
import co.com.bancolombia.model.message.Response;
import co.com.bancolombia.model.message.Sms;
import co.com.bancolombia.usecase.log.LogUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.function.Predicate;

import static co.com.bancolombia.commons.constants.Constants.NOT_SENT;
import static co.com.bancolombia.commons.constants.Constants.SUCCESS;
import static co.com.bancolombia.commons.constants.Medium.SMS;
import static co.com.bancolombia.commons.constants.TypeLogSend.SEND_220;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.INVALID_CONTACT;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.INVALID_MESSAGE;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.REQUIRED_PRIORITY;
import static co.com.bancolombia.usecase.sendalert.commons.ValidateData.isValidMobile;

@RequiredArgsConstructor
public class RouterProviderSMSUseCase {
    private final CommandGateway commandGateway;
    private final LogUseCase logUseCase;

    private static final Predicate<Message> validatePreference = message ->
            (message.getRetrieveInformation() && message.getPreferences().contains(SMS)) ||
                    (!message.getRetrieveInformation() && !message.getPhone().isEmpty());

    private Mono<Response> sendSMS(Message message, Alert alert, Response response) {
        return Mono.just(Sms.builder()
                        .trackId(message.getLogKey())
                        .priority(alert.getPriority())
                        .destination(Sms.To.builder().phoneNumber(message.getPhone())
                                .prefix(message.getPhoneIndicator()).build())
                        .message(alert.getMessage())
                        .provider(alert.getProviderSms())
                        .url(message.getUrl())
                        .build())
                .flatMap(commandGateway::sendCommandAlertSms)
                .thenReturn(response);
    }

    public Mono<Response> sendAlertToSMS(Alert alert, Message message) {
        return Mono.just(message)
                .filter(isValidMobile)
                .switchIfEmpty(Mono.error(new BusinessException(INVALID_CONTACT)))
                .filter(message1 -> !alert.getMessage().isEmpty())
                .switchIfEmpty(Mono.error(new BusinessException(INVALID_MESSAGE)))
                .filter(message1 -> Objects.nonNull(alert.getPriority()))
                .switchIfEmpty(Mono.error(new BusinessException(REQUIRED_PRIORITY)))
                .map(message1 -> new Response(0, SUCCESS, SMS, alert.getId()))
                .flatMap(response -> logUseCase.sendLogSMS(message, alert, SEND_220, response))
                .flatMap(response -> sendSMS(message, alert, response));
    }

    public Mono<Response> routeAlertsSMS(Message pMessage, Alert alert) {
        return Mono.just(pMessage)
                .filter(validatePreference)
                .flatMap(message -> sendAlertToSMS(alert, message))
                .switchIfEmpty(Mono.just(new Response(1, NOT_SENT, SMS, alert.getId())))
                .onErrorResume(e -> logUseCase.sendLogSMS(pMessage, alert, SEND_220,
                        new Response(1, e.getMessage(), SMS, alert.getId())));
    }

}
