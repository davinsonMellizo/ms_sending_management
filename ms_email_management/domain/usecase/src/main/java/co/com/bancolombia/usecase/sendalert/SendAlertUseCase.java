package co.com.bancolombia.usecase.sendalert;

import co.com.bancolombia.model.message.Alert;
import co.com.bancolombia.model.message.Mail;
import co.com.bancolombia.model.message.Recipient;
import co.com.bancolombia.model.message.Response;
import co.com.bancolombia.model.message.TemplateEmail;
import co.com.bancolombia.model.message.TemplateMasivian;
import co.com.bancolombia.model.message.gateways.MasivianGateway;
import co.com.bancolombia.model.message.gateways.SesGateway;
import co.com.bancolombia.model.message.gateways.TemplateEmailGateway;
import co.com.bancolombia.usecase.log.LogUseCase;
import co.com.bancolombia.usecase.sendalert.commons.Util;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

import static co.com.bancolombia.commons.constants.Provider.MASIVIAN;
import static co.com.bancolombia.commons.constants.Provider.SES;
import static co.com.bancolombia.commons.enums.TemplateType.SIMPLE;
import static co.com.bancolombia.usecase.sendalert.commons.Medium.EMAIL;

@RequiredArgsConstructor
public class SendAlertUseCase {
    private final TemplateEmailGateway templateEmailGateway;
    private final MasivianGateway masivianGateway;
    private final SesGateway sesGateway;
    private final LogUseCase logUseCase;
    private final GeneratorTokenUseCase generatorTokenUseCase;
    private static final int CONSTANT =23;

    public Mono<Void> sendAlert(Alert alert) {
        System.out.println("Send useCase 2");
        return templateEmailGateway.findTemplateEmail(alert.getTemplate().getName())
                .flatMap(templateEmail -> Util.replaceParameter(alert, templateEmail))
                .flatMap(templateEmail -> sendAlertToProviders(alert, templateEmail));
    }

    public Mono<Void> sendAlertToProviders(Alert alert, TemplateEmail templateEmail) {
        System.out.println("Send useCase 1 ");
        return validateAttachments(alert)
                .flatMap(alert1 ->  sendEmailByMasivian(alert1, templateEmail))
                .concatWith(sendEmailBySes(alert, templateEmail))
                .thenEmpty(Mono.empty());
    }

    private Mono<Response> sendEmailBySes(Alert alert, TemplateEmail templateEmail) {
        System.out.println("Send useCase");
        return Mono.just(alert.getProvider())
                .log("info")
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
        final String[] tokenTemp = {""};
        return Mono.just(alert.getProvider())
                .filter(provider -> provider.equalsIgnoreCase(MASIVIAN))
                .flatMap(provider ->generatorTokenUseCase.getNameToken(alert))
                .map(nameToken -> Mail.builder()
                        .From(alert.getFrom())
                        .Subject(templateEmail.getSubject())
                        .Recipients(recipients)
                        .Template(new TemplateMasivian(SIMPLE.getType(), templateEmail.getBodyHtml()))
                        .Attachments(alert.getAttachments())
                        .parameters(new ArrayList<>())
                        .nameToken(nameToken)
                        .build())
                .flatMap(mail->generatorTokenUseCase.getToken(mail))
                .doOnNext(getHeaders-> {tokenTemp[0] = String.valueOf(getHeaders.getHeaders()); })
                .flatMap(masivianGateway::sendMAIL)
                .doOnError(e -> Response.builder().code(1).description(e.getMessage()).build())
                .flatMap(response -> logUseCase.sendLog(alert, templateEmail, EMAIL, response))
                .onErrorResume(error -> filterErrorMAS(error, alert, tokenTemp[0],templateEmail))
                .map(o->(Response) o);
    }
    private Mono<Void> filterErrorMAS(Throwable error, Alert alert, String tokentemp,TemplateEmail templateEmail) {
        return Mono.just(error)
                .filter(catchError -> catchError.getMessage().contains("401"))
                .flatMap(next-> replaceBadToken(alert,tokentemp.substring(tokentemp.indexOf("bearer") + CONSTANT,
                        tokentemp.indexOf("}")),templateEmail))
                .switchIfEmpty(Mono.error(error));

    }
    private Mono<Void> replaceBadToken(Alert alert, String tokentemp, TemplateEmail templateEmail){
        return generatorTokenUseCase.deleteToken(tokentemp,alert)
                .map(d->new Response())
                .switchIfEmpty(sendEmailByMasivian(alert,templateEmail))
                .then(Mono.empty());

    }
}
