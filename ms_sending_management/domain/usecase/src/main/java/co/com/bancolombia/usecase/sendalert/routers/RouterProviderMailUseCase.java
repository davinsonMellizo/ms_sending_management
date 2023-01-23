package co.com.bancolombia.usecase.sendalert.routers;

import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.events.gateways.CommandGateway;
import co.com.bancolombia.model.message.Mail;
import co.com.bancolombia.model.message.Message;
import co.com.bancolombia.model.message.Recipient;
import co.com.bancolombia.model.message.Response;
import co.com.bancolombia.model.message.Template;
import co.com.bancolombia.model.provider.Provider;
import co.com.bancolombia.model.provider.gateways.ProviderGateway;
import co.com.bancolombia.model.remitter.Remitter;
import co.com.bancolombia.model.remitter.gateways.RemitterGateway;
import co.com.bancolombia.usecase.log.LogUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

import static co.com.bancolombia.commons.constants.TypeLogSend.SEND_220;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.INVALID_CONTACT;
import static co.com.bancolombia.usecase.sendalert.commons.ValidateData.isValidMailFormat;

@RequiredArgsConstructor
public class RouterProviderMailUseCase {
    private final RemitterGateway remitterGateway;
    private final ProviderGateway providerGateway;
    private final CommandGateway commandGateway;
    private final LogUseCase logUseCase;

    public Mono<Response> routeAlertMail(Message message, Alert alert) {
        return Mono.just(message)
                .filter(isValidMailFormat)
                .filter(message1 -> message1.getPreferences().contains("MAIL") || message1.getPreferences().isEmpty())
                .switchIfEmpty(Mono.error(new BusinessException(INVALID_CONTACT)))
                .flatMap(message1 -> getRemitter(alert, message))
                .zipWith(providerGateway.findProviderById(alert.getIdProviderMail()))
                .flatMap(data -> buildMail(message, alert, data.getT1(), data.getT2()))
                .onErrorResume(e -> logUseCase.sendLogMAIL(message, alert, SEND_220,
                        new Response(1, e.getMessage())));
    }

    private Mono<Remitter> getRemitter(Alert alert, Message message){
        return Mono.just(message)
                .filter(Message::getRetrieveInformation)
                .flatMap(message1 -> remitterGateway.findRemitterById(alert.getIdRemitter()))
                .switchIfEmpty(Mono.just(Remitter.builder().mail(message.getRemitter()).build()));
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
                .flatMap(commandGateway::sendCommandAlertEmail);
    }
}
