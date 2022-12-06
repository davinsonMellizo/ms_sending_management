package co.com.bancolombia.usecase.sendalert;


import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.events.gateways.CommandGateway;
import co.com.bancolombia.model.message.Message;
import co.com.bancolombia.model.message.Response;
import co.com.bancolombia.model.message.Sms;
import co.com.bancolombia.model.priority.Priority;
import co.com.bancolombia.model.priority.gateways.PriorityGateway;
import co.com.bancolombia.model.provider.Provider;
import co.com.bancolombia.model.provider.gateways.ProviderGateway;
import co.com.bancolombia.usecase.log.LogUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static co.com.bancolombia.commons.constants.TypeLogSend.SEND_220;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.INVALID_CONTACT;
import static co.com.bancolombia.usecase.sendalert.commons.ValidateData.isValidMobile;

@RequiredArgsConstructor
public class RouterProviderSMSUseCase {
    private final ProviderGateway providerGateway;
    private final PriorityGateway priorityGateway;
    private final CommandGateway commandGateway;
    private final LogUseCase logUseCase;

    public Mono<Response> validateMobile(Message message, Alert alert) {
        return Mono.just(message)
                .filter(isValidMobile)
                .switchIfEmpty(logUseCase.sendLogSMS(message, alert, SEND_220, new Response(1, INVALID_CONTACT)))
                .flatMap(prefix -> routeAlertsSMS(message, alert));
    }

    public Mono<Response> routeAlertsSMS(Message message, Alert alert) {
        return providerGateway.findProviderById(alert.getIdProviderSms())
                .zipWith(priorityGateway.findPriorityById(alert.getPriority()))
                .flatMap(data -> sendAlertToProviders(alert, message, data.getT1(), data.getT2()))
                .doOnError(e -> logUseCase.sendLogSMS(message, alert, SEND_220, new Response(1, e.getMessage())));
    }

    private Mono<Response> sendAlertToProviders(Alert alert, Message message, Provider provider, Priority priority) {
        return logUseCase.sendLogSMS(message, alert, SEND_220, new Response(0, "Success"))
                .cast(Response.class)
                .concatWith(sendSMS(message, alert.toBuilder().priority(priority.getCode()).build(), provider)).next();
    }

    private Mono<Response> sendSMS(Message message, Alert alert, Provider provider) {
        return Mono.just(Sms.builder()
                .logKey(message.getLogKey())
                .priority(message.getPriority() != null? message.getPriority(): alert.getPriority())
                .to(message.getPhoneIndicator() + message.getPhone())
                .message(alert.getMessage())
                .provider(provider.getId())
                .url(message.getUrl())
                .build())
                .flatMap(commandGateway::sendCommandAlertSms)
                .doOnError(e -> Response.builder().code(1).description(e.getMessage()).build());
    }


}
