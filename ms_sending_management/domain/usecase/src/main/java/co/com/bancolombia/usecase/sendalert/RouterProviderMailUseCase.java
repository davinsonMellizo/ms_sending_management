package co.com.bancolombia.usecase.sendalert;

import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.alerttemplate.AlertTemplate;
import co.com.bancolombia.model.alerttemplate.gateways.AlertTemplateGateway;
import co.com.bancolombia.model.message.*;
import co.com.bancolombia.model.message.gateways.MasivianGateway;
import co.com.bancolombia.model.remitter.gateways.RemitterGateway;
import co.com.bancolombia.model.message.Response;
import co.com.bancolombia.usecase.log.LogUseCase;
import co.com.bancolombia.usecase.sendalert.commons.BuilderMasivian;
import co.com.bancolombia.usecase.sendalert.commons.Util;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static co.com.bancolombia.commons.constants.TypeLogSend.SEND_220;
import static co.com.bancolombia.commons.constants.TypeLogSend.SEND_230;
import static co.com.bancolombia.commons.enums.TemplateType.COMPLEX;

@RequiredArgsConstructor
public class RouterProviderMailUseCase {
    private final AlertTemplateGateway alertTemplateGateway;
    private final MasivianGateway masivianGateway;
    private final RemitterGateway remitterGateway;
    private final LogUseCase logUseCase;

    public Mono<Response> sendAlertMail(Alert alert, Message message){
        return alertTemplateGateway.findTemplateById(alert.getIdTemplate())
                .doOnNext(alertTemplate -> message.setTemplate(alertTemplate.getId()))
                .flatMap(alertTemplate -> BuilderMasivian.buildParameter(alertTemplate, alert.getMessage()))
                .collectList()
                .flatMap(parameters -> sendEmail(message, alert, parameters));
    }

    public Mono<Response> sendEmail(Message message, Alert alert, List<Parameter> parameters){
        ArrayList<Recipient> recipients = new ArrayList<>();
        recipients.add(new Recipient(message.getMail()));
        return remitterGateway.findRemitterById(alert.getIdRemitter())
                .doOnError(e -> logUseCase.sendLogSMS(message, alert, SEND_220,
                        message.getParameters().get(0).getValue(), new Response(1, e.getMessage())))
                .map(remitter -> Mail.builder()
                        .Subject(alert.getSubjectMail())
                        .Recipients(recipients)
                        .Template(new Template(COMPLEX.getType(), message.getTemplate()))
                        .parameters(parameters)
                        .Attachments(Util.validateAttachments(message.getAttachments()))
                        .From(remitter.getMail())
                        .build())
                .flatMap(masivianGateway::sendMAIL)
                .doOnError(e -> Response.builder().code(1).description(e.getMessage()).build())
                .flatMap(response -> logUseCase.sendLogMAIL(message, alert, SEND_230,
                        message.getParameters().get(0).getValue(), response));
    }
}
