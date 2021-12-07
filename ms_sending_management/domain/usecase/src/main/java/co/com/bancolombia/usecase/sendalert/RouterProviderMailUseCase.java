package co.com.bancolombia.usecase.sendalert;

import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.alerttemplate.gateways.AlertTemplateGateway;
import co.com.bancolombia.model.events.gateways.CommandGateway;
import co.com.bancolombia.model.message.*;
import co.com.bancolombia.model.provider.Provider;
import co.com.bancolombia.model.provider.gateways.ProviderGateway;
import co.com.bancolombia.model.remitter.Remitter;
import co.com.bancolombia.model.remitter.gateways.RemitterGateway;
import co.com.bancolombia.usecase.log.LogUseCase;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.UUID;

import static co.com.bancolombia.commons.constants.TypeLogSend.SEND_220;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.INVALID_CONTACT;
import static co.com.bancolombia.usecase.sendalert.commons.ValidateData.isValidMail;

@RequiredArgsConstructor
public class RouterProviderMailUseCase {
    private final RemitterGateway remitterGateway;
    private final ProviderGateway providerGateway;
    private final CommandGateway commandGateway;
    private final LogUseCase logUseCase;

    public Mono<Response> routeAlertMail(Message message, Alert alert) {
        return Mono.just(message)
                .filter(isValidMail)
                .switchIfEmpty(Mono.error(new BusinessException(INVALID_CONTACT)))
                .flatMap(message1 -> remitterGateway.findRemitterById(alert.getIdRemitter()))
                .zipWith(providerGateway.findProviderByProviderService(alert.getIdProviderMail()))
                .doOnError(e -> logUseCase.sendLogMAIL(message, alert, SEND_220, new Response(1, e.getMessage())))
                .flatMap(data -> buildMail(message, alert, data.getT1(), data.getT2()))
                .onErrorResume(BusinessException.class, e -> logUseCase.sendLogMAIL(message, alert, SEND_220,
                        new Response(1, e.getBusinessErrorMessage().getMessage())));
    }

    public Mono<Response> buildMail(Message message, Alert alert, Remitter remitter, Provider provider) {
        ArrayList<Recipient> recipients = new ArrayList<>();
        recipients.add(new Recipient(message.getMail()));
        return logUseCase.sendLogMAIL(message, alert, SEND_220, new Response(0, "Success"))
                .cast(Mail.class)
                .concatWith(Mono.just(Mail.builder()
                        .logKey(message.getLogKey())
                        .provider(provider.getId())
                        .from(remitter.getMail())
                        .destination(new Mail.Destination(message.getMail(), "", ""))
                        .attachments(message.getAttachments())
                        .template(new Template(message.getParameters(), alert.getTemplateName())).build())).next()
                .flatMap(commandGateway::sendCommandAlertEmail)
                .doOnError(e -> Response.builder().code(1).description(e.getMessage()).build())
                .flatMap(response -> logUseCase.sendLogMAIL(message, alert, SEND_220, response));
    }
}
