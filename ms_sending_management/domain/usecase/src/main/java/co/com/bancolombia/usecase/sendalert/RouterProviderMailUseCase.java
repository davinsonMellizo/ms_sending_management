package co.com.bancolombia.usecase.sendalert;

import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.alerttemplate.gateways.AlertTemplateGateway;
import co.com.bancolombia.model.message.*;
import co.com.bancolombia.model.message.gateways.MasivianGateway;
import co.com.bancolombia.model.remitter.Remitter;
import co.com.bancolombia.model.remitter.gateways.RemitterGateway;
import co.com.bancolombia.usecase.log.LogUseCase;
import co.com.bancolombia.usecase.sendalert.commons.Util;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static co.com.bancolombia.commons.constants.TypeLogSend.SEND_220;
import static co.com.bancolombia.commons.constants.TypeLogSend.SEND_230;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.INVALID_CONTACT;
import static co.com.bancolombia.commons.enums.TemplateType.COMPLEX;
import static co.com.bancolombia.usecase.sendalert.commons.ValidateData.isValidMail;

@RequiredArgsConstructor
public class RouterProviderMailUseCase {
    private final AlertTemplateGateway alertTemplateGateway;
    private final MasivianGateway masivianGateway;
    private final RemitterGateway remitterGateway;
    private final LogUseCase logUseCase;


    public Mono<Response> sendAlertMail(Alert alert, Message pMessage) {
        return Mono.just(pMessage)
                .filter(isValidMail)
                .switchIfEmpty(Mono.error(new BusinessException(INVALID_CONTACT)))
                .flatMapMany(message -> alertTemplateGateway.findTemplateById(alert.getIdTemplate()))
                .doOnNext(alertTemplate -> pMessage.setTemplate(alertTemplate.getId()))
                .flatMap(alertTemplate -> Util.buildParameter(alertTemplate, alert.getMessage()))
                .collectList()
                .filter(parameters -> !parameters.isEmpty())
                .flatMap(parameters -> routeAlertMail(pMessage, alert, parameters))
                .onErrorResume(BusinessException.class, e -> logUseCase.sendLogMAIL(pMessage, alert, SEND_220,
                        alert.getMessage(), new Response(1, e.getBusinessErrorMessage().getMessage())));
    }

    public Mono<Response> routeAlertMail(Message message, Alert alert, List<Parameter> parameters) {
        return remitterGateway.findRemitterById(alert.getIdRemitter())
                .doOnError(e -> logUseCase.sendLogSMS(message, alert, SEND_220,
                        parameters.toString(), new Response(1, e.getMessage())))
                .flatMap(remitter -> buildMail(message, alert, parameters, remitter));
    }

    public Mono<Response> buildMail(Message message, Alert alert, List<Parameter> parameters, Remitter remitter) {
        ArrayList<Recipient> recipients = new ArrayList<>();
        recipients.add(new Recipient(message.getMail()));
        return logUseCase.sendLogMAIL(message, alert, SEND_220, alert.getMessage(),
                new Response(0, "Success"))
                .cast(Mail.class)
                .concatWith(Mono.just(Mail.builder()
                        .Subject(alert.getSubjectMail())
                        .Recipients(recipients)
                        .Template(new Template(COMPLEX.getType(), message.getTemplate()))
                        .parameters(parameters)
                        .Attachments(Util.validateAttachments(message.getAttachments()))
                        .From(remitter.getMail())
                        .build())).next()
                .flatMap(masivianGateway::sendMAIL)
                .doOnError(e -> Response.builder().code(1).description(e.getMessage()).build())
                .flatMap(response -> logUseCase.sendLogMAIL(message, alert, SEND_230,
                        alert.getMessage(), response));
    }
}
