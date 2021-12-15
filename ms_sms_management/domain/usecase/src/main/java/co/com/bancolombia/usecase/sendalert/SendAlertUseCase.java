package co.com.bancolombia.usecase.sendalert;

import co.com.bancolombia.model.message.*;
import co.com.bancolombia.model.message.gateways.InalambriaGateway;
import co.com.bancolombia.model.message.gateways.MasivianGateway;
import co.com.bancolombia.model.message.gateways.PinpointGateway;
import co.com.bancolombia.usecase.log.LogUseCase;
import co.com.bancolombia.usecase.sendalert.commons.Util;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.constants.Provider.*;
import static co.com.bancolombia.usecase.sendalert.commons.Medium.PUSH;
import static co.com.bancolombia.usecase.sendalert.commons.Medium.SMS;

@RequiredArgsConstructor
public class SendAlertUseCase {
    private final InalambriaGateway inalambriaGateway;
    private final MasivianGateway masivianGateway;
    private final PinpointGateway pinpointGateway;
    private final LogUseCase logUseCase;

    public Mono<Void> sendAlert(Alert alert) {
        return pinpointGateway.findTemplateSms(alert.getTemplate().getName())
                .flatMap(templateSms -> Util.replaceParameter(alert, templateSms))
                .flatMap(templateSms -> sendAlertToProviders(alert, templateSms));
    }

    public Mono<Void> sendAlertToProviders(Alert alert, TemplateSms templateSms) {
        return sendSMSInalambria(alert, templateSms)
                .concatWith(sendSMSMasivian(alert, templateSms))
                .thenEmpty(Mono.empty());
    }

    private Mono<Response> sendSMSInalambria(Alert alert, TemplateSms templateSms) {
        return Mono.just(alert.getProvider())
                .filter(provider -> provider.equalsIgnoreCase(INALAMBRIA))
                .map(provider -> SMSInalambria.builder()
                        .MessageText(templateSms.getBodyText())
                        .Devices(alert.getTo())
                        .Type(1)
                        .build())
                .flatMap(inalambriaGateway::sendSMS)
                .doOnError(e -> Response.builder().code(1).description(e.getMessage()).build())
                .flatMap(response -> logUseCase.sendLog(alert, templateSms, SMS, response));
    }

    private Mono<Response> sendSMSMasivian(Alert alert, TemplateSms templateSms) {
        return Mono.just(alert.getProvider())
                .filter(provider -> provider.equalsIgnoreCase(MASIVIAN))
                .map(provider -> Sms.builder()
                        .text(templateSms.getBodyText()).Longmessage(true)
                        .Url(alert.getUrl()).domainshorturl(false)
                        .To(alert.getTo())
                        .IsPremium(false).IsFlash(false)
                        .build())
                .flatMap(masivianGateway::sendSMS)
                .doOnError(e -> Response.builder().code(1).description(e.getMessage()).build())
                .flatMap(response -> logUseCase.sendLog(alert, templateSms, SMS, response));
    }
}
