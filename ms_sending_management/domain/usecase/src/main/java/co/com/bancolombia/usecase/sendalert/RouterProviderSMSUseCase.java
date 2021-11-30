package co.com.bancolombia.usecase.sendalert;


import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.events.gateways.CommandGateway;
import co.com.bancolombia.model.message.Message;
import co.com.bancolombia.model.message.Response;
import co.com.bancolombia.model.message.Sms;
import co.com.bancolombia.model.message.Template;
import co.com.bancolombia.model.prefix.gateways.PrefixRepository;
import co.com.bancolombia.model.provider.Provider;
import co.com.bancolombia.model.provider.gateways.ProviderGateway;
import co.com.bancolombia.model.providerservice.ProviderService;
import co.com.bancolombia.model.providerservice.gateways.ProviderServiceGateway;
import co.com.bancolombia.usecase.log.LogUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static co.com.bancolombia.commons.constants.TypeLogSend.SEND_220;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.INVALID_CONTACT;
import static co.com.bancolombia.usecase.sendalert.commons.ValidateData.isValidMobile;

@RequiredArgsConstructor
public class RouterProviderSMSUseCase {
    private final ProviderServiceGateway providerServiceGateway;
    private final PrefixRepository prefixRepository;
    private final ProviderGateway providerGateway;
    private final CommandGateway commandGateway;
    private final LogUseCase logUseCase;

    public Mono<Response> validateMobile(Message message, Alert alert) {
        return Mono.just(message)
                .filter(isValidMobile)
                .map(message1 -> message.getPhone().substring(0, 3))
                .flatMap(prefixRepository::findPrefix)
                .switchIfEmpty(logUseCase.sendLogSMS(message, alert, SEND_220,
                        message.getParameters().get(0).getValue(), new Response(1, INVALID_CONTACT)))
                .flatMap(prefix -> routeAlertsSMS(message, alert));
    }

    public Mono<Response> routeAlertsSMS(Message message, Alert alert) {
        return providerServiceGateway.findProviderService(alert.getIdProviderSms())
                .map(ProviderService::getIdProvider)
                .flatMap(providerGateway::findProviderById)
                .flatMap(provider -> sendAlertToProviders(alert, message, provider))
                .doOnError(e -> logUseCase.sendLogSMS(message, alert, SEND_220,
                        message.getParameters().get(0).getValue(), new Response(1, e.getMessage())));
    }

    private Mono<Response> sendAlertToProviders(Alert alert, Message message, Provider provider) {
        return logUseCase.sendLogSMS(message, alert, SEND_220, alert.getMessage(), new Response(0, "Success"))
                .cast(Response.class)
                .concatWith(sendSMS(message, alert, provider)).next();
    }

    private Mono<Response> sendSMS(Message message, Alert alert, Provider provider) {
        return Mono.just(Sms.builder()
                .logKey(message.getLogKey())
                .priority(1)
                .to(message.getPhoneIndicator() + message.getPhone())
                .template(new Template(message.getParameters(), "Compra"))
                .text(alert.getMessage())
                .provider(provider.getId())
                .documentNumber(Long.toString(message.getDocumentNumber()))
                .documentType(Integer.toString(message.getDocumentType()))
                .url(message.getUrl())
                .build())
                .flatMap(commandGateway::sendCommandAlertSms)
                .doOnError(e -> Response.builder().code(1).description(e.getMessage()).build());
    }

}
