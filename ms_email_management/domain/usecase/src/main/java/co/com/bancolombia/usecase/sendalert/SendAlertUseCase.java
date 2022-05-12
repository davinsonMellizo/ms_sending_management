package co.com.bancolombia.usecase.sendalert;

import co.com.bancolombia.model.message.*;
import co.com.bancolombia.model.message.gateways.MasivianGateway;
import co.com.bancolombia.model.message.gateways.PinpointGateway;
import co.com.bancolombia.model.message.gateways.SesGateway;
import co.com.bancolombia.usecase.log.LogUseCase;
import co.com.bancolombia.usecase.sendalert.commons.Util;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

import static co.com.bancolombia.commons.constants.Provider.*;
import static co.com.bancolombia.commons.constants.TypeLogSend.SEND_230;
import static co.com.bancolombia.commons.enums.TemplateType.SIMPLE;
import static co.com.bancolombia.usecase.sendalert.commons.Medium.*;

@RequiredArgsConstructor
public class SendAlertUseCase {
    private final PinpointGateway pinpointGateway;
    private final MasivianGateway masivianGateway;
    private final SesGateway sesGateway;
    private final LogUseCase logUseCase;

    public Mono<Void> sendAlert(Alert alert) {
        return pinpointGateway.findTemplateEmail(alert.getTemplate().getName())
                .flatMap(templateEmail -> Util.replaceParameter(alert, templateEmail))
                .flatMap(templateEmail -> sendAlertToProviders(alert, templateEmail));
    }

    public Mono<Void> sendAlertToProviders(Alert alert, TemplateEmail templateEmail) {
        return validateAttachments(alert)
                .flatMap(alert1 ->  sendEmailByMasivian(alert1, templateEmail))
                .concatWith(sendEmailBySes(alert, templateEmail))
                .thenEmpty(Mono.empty());
    }

    private Mono<Response> sendEmailBySes(Alert alert, TemplateEmail templateEmail) {
        return Mono.just(alert.getProvider())
                .filter(provider -> provider.equalsIgnoreCase(SES))
                .flatMap(provider ->  sesGateway.sendEmail(templateEmail, alert))
                .doOnError(e -> Response.builder().code(1).description(e.getMessage()).build())
                .flatMap(response -> logUseCase.sendLog(alert, templateEmail, EMAIL, response));
    }

    private Mono<Alert> validateAttachments(Alert alert) {
        return Mono.just(alert);
    }

    public Mono<Response> sendEmailByMasivian(Alert alert, TemplateEmail templateEmail) {
        ArrayList<Recipient> recipients = new ArrayList<>();
        recipients.add(new Recipient(alert.getDestination().getToAddress()));
        return Mono.just(alert.getProvider())
                .filter(provider -> provider.equalsIgnoreCase(MASIVIAN))
                .map(provider -> Mail.builder()
                        .From(alert.getFrom())
                        .Subject(templateEmail.getSubject())
                        .Recipients(recipients)
                        .Template(new TemplateMasivian(SIMPLE.getType(), templateEmail.getBodyHtml()))
                        .Attachments(alert.getAttachments())
                        .build())
                .flatMap(masivianGateway::sendMAIL)
                .doOnError(e -> Response.builder().code(1).description(e.getMessage()).build())
                .flatMap(response -> logUseCase.sendLog(alert, templateEmail, EMAIL, response));
    }
}