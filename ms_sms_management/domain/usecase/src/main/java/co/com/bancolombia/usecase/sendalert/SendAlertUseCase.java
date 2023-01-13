package co.com.bancolombia.usecase.sendalert;

import co.com.bancolombia.model.message.Alert;
import co.com.bancolombia.model.message.Response;
import co.com.bancolombia.model.message.SMSInalambria;
import co.com.bancolombia.model.message.SMSMasiv;
import co.com.bancolombia.model.message.gateways.InalambriaGateway;
import co.com.bancolombia.model.message.gateways.MasivianGateway;
import co.com.bancolombia.usecase.log.LogUseCase;
import co.com.bancolombia.usecase.log.ValidationLogUtil;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.constants.Provider.INALAMBRIA;
import static co.com.bancolombia.commons.constants.Provider.MASIVIAN;
import static co.com.bancolombia.usecase.sendalert.commons.Medium.SMS;

@RequiredArgsConstructor
public class SendAlertUseCase {
    private final InalambriaGateway inalambriaGateway;
    private final MasivianGateway masivianGateway;
    private final LogUseCase logUseCase;
    private final GeneratorTokenUseCase generatorTokenUseCase;
    private static final  Integer CONSTANT = 23;

    public Mono<Void> sendAlert(Alert alert) {
        return sendSMSInalambria(alert)
                .concatWith(sendSMSMasivian(alert))
                .thenEmpty(Mono.empty());
    }

    private Mono<Response> sendSMSInalambria(Alert alert) {
        var tokenTemp = new String[]{""};
        return Mono.just(alert.getProvider())
                .filter(provider -> provider.equalsIgnoreCase(INALAMBRIA))
                .map(provider -> SMSInalambria.builder()
                        .MessageText(alert.getUrl().isEmpty() ? alert.getMessage() : (alert.getMessage() + " @Url"))
                        .Devices(alert.getTo())
                        .Url(alert.getUrl().isEmpty()?null: alert.getUrl())
                        .Type(1)
                        .build())
                .flatMap(data-> generatorTokenUseCase.getTokenINA(data, alert))
                .doOnNext(getHeaders-> tokenTemp[0] = String.valueOf(getHeaders.getHeaders()))
                .flatMap(inalambriaGateway::sendSMS)
                .doOnError(e -> Response.builder().code(1).description(e.getMessage()).build())
                .flatMap(response -> ValidationLogUtil.validSendLog(alert, SMS, response, logUseCase))
                .onErrorResume(error -> filterError(error, alert, tokenTemp[0]))
                .map(Response.class::cast);
    }

    private Mono<Response> sendSMSMasivian(Alert alert) {
        var tokenTemp = new String[]{""};
        return Mono.just(alert.getProvider())
                .filter(provider -> provider.equalsIgnoreCase(MASIVIAN))
                .map(provider -> SMSMasiv.builder()
                        .text(alert.getUrl().isEmpty() ? alert.getMessage() : (alert.getMessage() + " SHORTURL"))
                        .isLongmessage(true)
                        .shortUrlConfig(alert.getUrl().isEmpty()?null: SMSMasiv.ShortUrlConfig.builder()
                                .url(alert.getUrl()).build())
                        .to(alert.getTo())
                        .isPremium(false).isFlash(false)
                        .build())
                .flatMap(data->generatorTokenUseCase.getTokenMAS(data,alert))
                .doOnNext(getHeaders-> tokenTemp[0] = String.valueOf(getHeaders.getHeaders()))
                .flatMap(masivianGateway::sendSMS)
                .doOnError(e -> Response.builder().code(1).description(e.getMessage()).build())
                .flatMap(response -> ValidationLogUtil.validSendLog(alert, SMS, response, logUseCase))
                .onErrorResume(error -> filterError(error, alert, tokenTemp[0]))
                .map(Response.class::cast);
    }
    private Mono<Void> filterError(Throwable error, Alert alert, String tokentemp){
        return Mono.just(error)
                .filter(catchError -> catchError.getMessage().contains("401"))
                .flatMap(renew-> replaceBadToken(alert,tokentemp.substring(tokentemp
                        .indexOf("bearer")+23,tokentemp.indexOf("}"))))
                .switchIfEmpty(Mono.error(error));
    }
    private Mono<Void> replaceBadToken(Alert alert, String tokenTemp){
        return generatorTokenUseCase.deleteToken(tokenTemp,alert)
                .map(d->new Response())
                .switchIfEmpty(sendSMSInalambria(alert))
                .then(Mono.empty());
    }




}
