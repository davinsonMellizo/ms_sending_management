package co.com.bancolombia.usecase.sendalert;

import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.message.Mail;
import co.com.bancolombia.model.message.Message;
import co.com.bancolombia.model.message.Recipient;
import co.com.bancolombia.model.message.Template;
import co.com.bancolombia.model.remitter.gateways.RemitterGateway;
import co.com.bancolombia.usecase.Response;
import co.com.bancolombia.usecase.log.LogUseCase;
import co.com.bancolombia.usecase.sendalert.commons.Util;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

import static co.com.bancolombia.commons.constants.TypeLogSend.SEND_220;
import static co.com.bancolombia.commons.constants.TypeLogSend.SEND_230;
import static co.com.bancolombia.commons.enums.TemplateType.COMPLEX;

@RequiredArgsConstructor
public class RouterProviderMailUseCase {
    private final RemitterGateway remitterGateway;
    private final LogUseCase logUseCase;

    public Mono<Response> sendEmail(Message message, Alert alert){
        ArrayList<Recipient> recipients = new ArrayList<>();
        recipients.add(new Recipient(message.getMail()));
        return remitterGateway.findRemitterById(alert.getIdRemitter())
                .doOnError(e -> logUseCase.sendLogSMS(message, alert, SEND_220,
                        message.getParameters().get(0).getValue(), new Response(1, e.getMessage())))
                .map(remitter -> Mail.builder()
                        .Subject(alert.getSubjectMail())
                        .Recipients(recipients)
                        .Template(new Template(COMPLEX.getType(), message.getTemplate()))
                        .Attachments(Util.validateAttachments(message.getAttachments()))
                        .From(remitter.getMail())
                        .build())
                .map(mail -> Response.builder().code(200).build())//TODO call api Masivian EMAIL
                .doOnError(e -> Response.builder().code(1).description(e.getMessage()).build())
                .flatMap(response -> logUseCase.sendLogMAIL(message, alert, SEND_230,
                        message.getParameters().get(0).getValue(), response));
    }
}
