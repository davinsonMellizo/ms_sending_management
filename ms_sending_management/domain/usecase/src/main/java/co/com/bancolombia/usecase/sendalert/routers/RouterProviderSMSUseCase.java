package co.com.bancolombia.usecase.sendalert.routers;


import co.com.bancolombia.commons.exceptions.BusinessException;
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

import java.util.Objects;

import static co.com.bancolombia.commons.constants.Constants.PUSH_SMS;
import static co.com.bancolombia.commons.constants.Constants.NO;
import static co.com.bancolombia.commons.constants.TypeLogSend.SEND_220;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.INVALID_CONTACT;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.PRIORITY_INVALID;
import static co.com.bancolombia.usecase.sendalert.commons.ValidateData.isValidMobile;

@RequiredArgsConstructor
public class RouterProviderSMSUseCase {
    private final ProviderGateway providerGateway;
    private final PriorityGateway priorityGateway;
    private final CommandGateway commandGateway;
    private final LogUseCase logUseCase;

    public Mono<Response> routeAlertsSMS(Message message, Alert alert) {
        return Mono.just(message)
                .filter(isValidMobile)
                .switchIfEmpty(Mono.error(new BusinessException(INVALID_CONTACT)))
                .filter(message1 -> alert.getPush().equals(NO) || alert.getPush().equals(PUSH_SMS))
                .filter(message1 ->  !message1.getRetrieveInformation() || message1.getPreferences().contains("SMS") ||
                        message1.getPreferences().isEmpty())
                .flatMap(prefix -> providerGateway.findProviderById(alert.getIdProviderSms()))
                .zipWith(getPriority(message, alert))
                .flatMap(data -> sendAlertToProviders(alert, message, data.getT1(), data.getT2()))
                .onErrorResume(e -> logUseCase.sendLogSMS(message, alert, SEND_220, new Response(1, e.getMessage())));
    }

    private Mono<Priority> getPriority(Message message, Alert alert){
        return Mono.just(alert)
                .filter(alert1 -> Objects.isNull(alert1.getPriority()))
                .map(priority -> Priority.builder().code(message.getPriority()).build())
                .switchIfEmpty(priorityGateway.findPriorityById(alert.getPriority()))
                .filter(priority -> Objects.nonNull(priority.getCode()))
                .switchIfEmpty(Mono.error(new BusinessException(PRIORITY_INVALID)));
    }

    private Mono<Response> sendAlertToProviders(Alert alert, Message message, Provider provider, Priority priority) {
        return logUseCase.sendLogSMS(message, alert, SEND_220, new Response(0, "Success"))
                .cast(Response.class)
                .concatWith(sendSMS(message, alert.toBuilder().priority(priority.getCode()).build(), provider)).next();
    }

    private Mono<Response> sendSMS(Message message, Alert alert, Provider provider) {
        return Mono.just(Sms.builder()
                .logKey(message.getLogKey())
                .priority(alert.getPriority())
                .to(message.getPhoneIndicator() + message.getPhone())
                .message(alert.getMessage())
                .provider(provider.getId())
                .url(message.getUrl())
                .build())
                .flatMap(commandGateway::sendCommandAlertSms);
    }

}
