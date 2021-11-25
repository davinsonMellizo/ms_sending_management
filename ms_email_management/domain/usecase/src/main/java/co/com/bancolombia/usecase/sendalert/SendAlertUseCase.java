package co.com.bancolombia.usecase.sendalert;

import co.com.bancolombia.model.message.*;
import co.com.bancolombia.model.message.gateways.MasivianGateway;
import co.com.bancolombia.model.message.gateways.PinpointGateway;
import co.com.bancolombia.usecase.log.LogUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

import static co.com.bancolombia.commons.constants.Provider.*;
import static co.com.bancolombia.commons.constants.TypeLogSend.SEND_230;
import static co.com.bancolombia.commons.enums.TemplateType.COMPLEX;
import static co.com.bancolombia.commons.enums.TemplateType.SIMPLE;
import static co.com.bancolombia.usecase.sendalert.commons.Medium.*;

@RequiredArgsConstructor
public class SendAlertUseCase {
    private final MasivianGateway masivianGateway;
    private final PinpointGateway pinpointGateway;
    private final LogUseCase logUseCase;

    public Mono<Void> sendAlertToProviders(Alert alert) {
        return validateAttachments(alert)
                .flatMap(this::sendEmailByMasivian)
                .thenEmpty(Mono.empty());
    }

    private Mono<Alert> validateAttachments(Alert alert) {
        return Mono.just(alert);
    }

    public Mono<Response> sendEmailByMasivian(Alert alert) {
        ArrayList<Recipient> recipients = new ArrayList<>();
        //recipients.add(new Recipient(alert.getDestination().getToAddress()));
        return Mono.just(alert.getProvider())
                //.filter(provider -> provider.equalsIgnoreCase(MASIVIAN))
                .flatMap(provider -> pinpointGateway.findTemplateEmail(alert.getTemplate().getName()))
                .then(Mono.empty())
                /*.doOnNext(template -> alert.setMessage(Alert.Message.builder().body(template.getBody()).build()))
                .map(template -> Mail.builder()
                        .From(alert.getFrom())
                        .Subject(template.getSubject())
                        .Recipients(recipients)
                        .Template(new TemplateMasivian(SIMPLE.getType(), template.getBody()))
                        .Attachments(alert.getAttachments())
                        .build())
                .flatMap(masivianGateway::sendMAIL)
                .doOnError(e -> Response.builder().code(1).description(e.getMessage()).build())
                .flatMap(response -> logUseCase.sendLog(alert, SEND_230, EMAIL, response))*/;
    }
}
