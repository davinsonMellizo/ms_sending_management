package co.com.bancolombia.usecase.sendalert.routers;

import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.events.gateways.CommandGateway;
import co.com.bancolombia.model.message.Mail;
import co.com.bancolombia.model.message.Message;
import co.com.bancolombia.model.message.Recipient;
import co.com.bancolombia.model.message.Response;
import co.com.bancolombia.model.message.Template;
import co.com.bancolombia.usecase.log.LogUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.function.Predicate;

import static co.com.bancolombia.commons.constants.Constants.NOT_SENT;
import static co.com.bancolombia.commons.constants.Constants.SUCCESS;
import static co.com.bancolombia.commons.constants.Medium.MAIL;
import static co.com.bancolombia.commons.constants.TypeLogSend.SEND_220;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.*;
import static co.com.bancolombia.usecase.sendalert.commons.ValidateData.isValidMailFormat;

@RequiredArgsConstructor
public class RouterProviderMailUseCase {
    private final CommandGateway commandGateway;
    private final LogUseCase logUseCase;

    private static final Predicate<Message> validatePreference = message ->
            (message.getRetrieveInformation() &&
                    ((message.getPreferences().contains(MAIL)) || message.getPreferences().isEmpty()))||
                    (!message.getRetrieveInformation() && !message.getMail().isEmpty());

    private Mono<Message> validateData(Message message, Alert alert) {
        return Mono.just(message)
                .filter(isValidMailFormat)
                .switchIfEmpty(Mono.error(new BusinessException(INVALID_CONTACT)))
                .filter(message1 -> !message.getRemitter().isEmpty())
                .switchIfEmpty(Mono.error(new BusinessException(REQUIRED_REMITTER)))
                .filter(message1 -> !alert.getTemplateName().isEmpty())
                .switchIfEmpty(Mono.error(new BusinessException(TEMPLATE_INVALID)));
    }

    public Mono<Mail> buildMail(Message message, Alert alert) {
        ArrayList<Recipient> recipients = new ArrayList<>();
        recipients.add(new Recipient(message.getMail()));
        return Mono.just(Mail.builder()
                        .logKey(message.getLogKey())
                        .provider(alert.getProviderMail())
                        .from(alert.getRemitter())
                        .category(message.getCategory())
                        .destination(new Mail.Destination(message.getMail(), "", ""))
                        .attachments(message.getAttachments())
                        .template(new Template(message.getParameters(), alert.getTemplateName())).build());
    }

    public Mono<Response> routeAlertMail(Message message, Alert alert) {
        return Mono.just(message)
                .filter(validatePreference)
                .flatMap(message1 -> validateData(message, alert))
                .map(message1 -> new Response(0, SUCCESS))
                .flatMap(response -> logUseCase.sendLogMAIL(message, alert, SEND_220, response))
                .flatMap(message1 -> buildMail(message, alert))
                .flatMap(commandGateway::sendCommandAlertEmail)
                .onErrorResume(e -> logUseCase.sendLogMAIL(message, alert, SEND_220,
                        new Response(1, e.getMessage())))
                .switchIfEmpty(Mono.just(new Response(1, NOT_SENT)));
    }
}
