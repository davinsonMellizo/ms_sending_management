package co.com.bancolombia.usecase.sendalert;

import co.com.bancolombia.model.message.*;
import co.com.bancolombia.model.message.gateways.InalambriaGateway;
import co.com.bancolombia.model.message.gateways.MasivianGateway;
import co.com.bancolombia.usecase.log.LogUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.Map;

import static co.com.bancolombia.commons.constants.Provider.*;
import static co.com.bancolombia.usecase.sendalert.commons.Medium.SMS;

@RequiredArgsConstructor
public class SendAlertUseCase {
    private final InalambriaGateway inalambriaGateway;
    private final MasivianGateway masivianGateway;
    private final LogUseCase logUseCase;

    public Mono<Void> sendAlert(Alert alert) {
        return sendSMSInalambria(alert)
                .concatWith(sendSMSMasivian(alert))
                .thenEmpty(Mono.empty());
    }

    private Mono<Response> sendSMSInalambria(Alert alert) {
        System.out.println("para enviar");
        return Mono.just(alert.getProvider())
                .filter(provider -> provider.equalsIgnoreCase(INALAMBRIA))
                .map(provider -> SMSInalambria.builder()
                        .MessageText(alert.getUrl().isEmpty()?alert.getMessage(): alert.getMessage() + " @Url")
                        .Devices(alert.getTo())
                        .Url(alert.getUrl().isEmpty()?null: alert.getUrl())
                        .Type(1)
                        .build())
                .flatMap(inalambriaGateway::sendSMS)
                .doOnError(e -> Response.builder().code(1).description(e.getMessage()).build())
                .flatMap(response -> logUseCase.sendLog(alert, SMS, response));
    }

    private Mono<Response> sendSMSMasivian(Alert alert) {
        return Mono.just(alert.getProvider())
                .filter(provider -> provider.equalsIgnoreCase(MASIVIAN))
                .map(provider -> Sms.builder()
                        .text(alert.getUrl().isEmpty()? alert.getMessage(): alert.getMessage() + " SHORTURL")
                        .isLongmessage(true)
                        .shortUrlConfig(alert.getUrl().isEmpty()?null: Sms.ShortUrlConfig.builder()
                                .url(alert.getUrl()).build())
                        .to(alert.getTo())
                        .isPremium(false).isFlash(false)
                        .build())
                .flatMap(masivianGateway::sendSMS)
                .doOnError(e -> Response.builder().code(1).description(e.getMessage()).build())
                .flatMap(response -> logUseCase.sendLog(alert, SMS, response));
    }

}
