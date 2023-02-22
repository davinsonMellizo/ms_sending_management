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
import reactor.util.function.Tuple2;

import java.util.ArrayList;

import static co.com.bancolombia.commons.constants.TypeLogSend.SEND_220;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.INVALID_CONTACT;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.INVALID_REMITTER;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.TEMPLATE_INVALID;
import static co.com.bancolombia.usecase.sendalert.commons.ValidateData.isValidMailFormat;

@RequiredArgsConstructor
public class RouterProviderMailUseCase {
    private final CommandGateway commandGateway;
    private final LogUseCase logUseCase;

    public Mono<Response> routeAlertMail(Message message, Alert alert) {
        return Mono.just(message)
                .filter(isValidMailFormat)
                .switchIfEmpty(Mono.error(new BusinessException(INVALID_CONTACT)))
                .filter(message1 -> !alert.getTemplateName().isEmpty())
                .switchIfEmpty(Mono.error(new BusinessException(TEMPLATE_INVALID)))
                .filter(message1 -> message1.getPreferences().contains("MAIL") || message1.getPreferences().isEmpty())
                .flatMap(message1 -> getRemitter(alert, message))
                .flatMap(message1 -> buildMail(message1, alert))
                .flatMap(commandGateway::sendCommandAlertEmail)
                .zipWith(logUseCase.sendLogMAIL(message, alert, SEND_220, new Response(0, "Success")))
                .map(Tuple2::getT2)
                .onErrorResume(e -> logUseCase.sendLogMAIL(message, alert, SEND_220,
                        new Response(1, e.getMessage())))
                .switchIfEmpty(Mono.just(new Response()));
    }

    private Mono<Message> getRemitter(Alert alert, Message message){
        return Mono.just(message)
                .filter(message1 -> !message1.getAlert().isEmpty() || !message.getTransactionCode().isEmpty())
                .map(message1 -> message.toBuilder().remitter(alert.getRemitter()).build())
                .switchIfEmpty(Mono.just(message))
                .filter(message1 -> !message1.getRemitter().isEmpty())
                .switchIfEmpty(Mono.error(new BusinessException(INVALID_REMITTER)));
    }

    public Mono<Mail> buildMail(Message message, Alert alert) {
        ArrayList<Recipient> recipients = new ArrayList<>();
        recipients.add(new Recipient(message.getMail()));
        return Mono.just(Mail.builder()
                        .logKey(message.getLogKey())
                        .provider(alert.getProviderMail())
                        .from(message.getRemitter())
                        .category(message.getCategory())
                        .category(message.getCategory())
                        .destination(new Mail.Destination(message.getMail(), "", ""))
                        .attachments(message.getAttachments())
                        .template(new Template(message.getParameters(), alert.getTemplateName())).build());
    }
}
