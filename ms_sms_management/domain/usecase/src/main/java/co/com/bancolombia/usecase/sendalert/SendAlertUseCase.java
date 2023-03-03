package co.com.bancolombia.usecase.sendalert;


import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.message.Alert;
import co.com.bancolombia.model.message.Response;
import co.com.bancolombia.model.message.SMSInalambria;
import co.com.bancolombia.model.message.SMSInfobip;
import co.com.bancolombia.model.message.SMSMasiv;
import co.com.bancolombia.model.message.TemplateSms;
import co.com.bancolombia.model.message.gateways.InalambriaGateway;
import co.com.bancolombia.model.message.gateways.InfobipGateway;
import co.com.bancolombia.model.message.gateways.MasivianGateway;
import co.com.bancolombia.model.message.gateways.TemplateGateway;
import co.com.bancolombia.usecase.log.LogUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.constants.Provider.*;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.REQUIRED_MESSAGE_TEMPLATE;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.TEMPLATE_NOT_FOUND;
import static co.com.bancolombia.usecase.sendalert.commons.Medium.SMS;

@RequiredArgsConstructor
public class SendAlertUseCase {
    private final InalambriaGateway inalambriaGateway;
    private final MasivianGateway masivianGateway;

    private final InfobipGateway infobipGateway;

    private final TemplateGateway templateGateway;

    private final LogUseCase logUseCase;
    private final GeneratorTokenUseCase generatorTokenUseCase;
    private static final  Integer CONSTANT = 23;
    private static final String SENDER = "InfoSMS";
    private static final Integer STATUS_UNAUTHORIZED =401;

    public Mono<Response> sendAlert(Alert pAlert) {
        return  Mono.just(pAlert)
                .filter(alert -> !alert.getMessage().isEmpty() || (!alert.getTemplate().getName().isEmpty()))
                .flatMap(this::template)
                .switchIfEmpty(Mono.error(new BusinessException(REQUIRED_MESSAGE_TEMPLATE)))
                .log()
                .flatMap(templateSms -> sendAlertToProviders(pAlert, templateSms));

    }
    public Mono<Response> sendAlertToProviders(Alert alert, TemplateSms templateSms) {
        return sendSMSInalambria(alert, templateSms)
                .concatWith(sendSMSMasivian(alert, templateSms))
                .concatWith(sendSMSInfobip(alert, templateSms))
                .last();
    }

    private Mono<Response> sendSMSInalambria(Alert alert,TemplateSms templateSms ) {
        var tokenTemp = new String[]{""};
        return Mono.just(alert.getProvider())
                .filter(provider -> provider.equalsIgnoreCase(INALAMBRIA))
                .map(provider -> SMSInalambria.builder()
                        .MessageText(alert.getUrlForShortening().isEmpty()
                                ? templateSms.getBodyText() : (templateSms.getBodyText() + " @Url"))
                        .Devices(alert.getTo().getPhoneIndicator()+alert.getTo().getPhoneNumber())
                        .Url(alert.getUrlForShortening().isEmpty()?null: alert.getUrlForShortening())
                        .Type(1)
                        .build())
                .flatMap(data-> generatorTokenUseCase.getTokenINA(data, alert))
                .doOnNext(getHeaders-> tokenTemp[0] = String.valueOf(getHeaders.getHeaders()))
                .flatMap(inalambriaGateway::sendSMS)
                .flatMap(response -> logUseCase.handlerLog(alert, SMS, response, !response.getCode()
                        .equals(STATUS_UNAUTHORIZED)))
                .flatMap(response -> filterError(response, alert, tokenTemp[0],templateSms));
    }

    private Mono<Response> sendSMSMasivian(Alert alert,  TemplateSms templateSms) {
        var tokenTemp = new String[]{""};
        return Mono.just(alert.getProvider())
                .filter(provider -> provider.equalsIgnoreCase(MASIVIAN))
                .map(provider -> SMSMasiv.builder()
                        .text(alert.getUrlForShortening().isEmpty()
                                ? templateSms.getBodyText() : (templateSms.getBodyText() + " SHORTURL"))
                        .isLongmessage(true)
                        .shortUrlConfig(alert.getUrlForShortening().isEmpty()?null: SMSMasiv.ShortUrlConfig.builder()
                                .url(alert.getUrlForShortening()).build())
                        .to(alert.getTo().getPhoneIndicator()+alert.getTo().getPhoneNumber())
                        .isPremium(false).isFlash(false)
                        .build())
                .flatMap(data->generatorTokenUseCase.getTokenMAS(data,alert))
                .doOnNext(getHeaders-> tokenTemp[0] = String.valueOf(getHeaders.getHeaders()))
                .flatMap(masivianGateway::sendSMS)
                .flatMap(response -> logUseCase.handlerLog(alert, SMS, response, !response.getCode()
                        .equals(STATUS_UNAUTHORIZED)))
                .flatMap(response -> filterError(response, alert, tokenTemp[0],templateSms));
    }

    private Mono<Response> sendSMSInfobip(Alert alert,  TemplateSms templateSms) {
        var tokenTemp = new String[]{""};
        return Mono.just(alert.getProvider())
                .filter(provider -> provider.equalsIgnoreCase(INFOBIP))
                .map(provider -> SMSInfobip.builder()
                        .message(SMSInfobip.Message.builder()
                                .from("InfobipSMS")
                                .destination(SMSInfobip.Destination.builder().to(alert.getTo().getPhoneIndicator()
                                        +alert.getTo().getPhoneNumber()).build())
                                .text(templateSms.getBodyText())
                                .build())
                        .build())
                .flatMap(data->generatorTokenUseCase.getTokenInf(data,alert))
                .doOnNext(getHeaders-> tokenTemp[0] = String.valueOf(getHeaders.getHeaders()))
                .flatMap(infobipGateway::sendSMS)
                .flatMap(response -> logUseCase.handlerLog(alert, SMS, response, !response.getCode()
                        .equals(STATUS_UNAUTHORIZED)))
                .flatMap(response -> filterError(response, alert, tokenTemp[0],templateSms));
    }

    private Mono<Response> filterError( Response response, Alert alert, String tokentemp, TemplateSms templateSms){
        return Mono.just(response)
                .filter(res -> res.getCode().equals(STATUS_UNAUTHORIZED))
                .flatMap(renew-> replaceBadToken(alert,tokentemp.substring(tokentemp
                        .indexOf("bearer")+CONSTANT,tokentemp.indexOf("}")),templateSms))
                .switchIfEmpty(Mono.just(response));
    }
    private Mono<Response> replaceBadToken(Alert alert, String tokenTemp, TemplateSms templateSms){
        return generatorTokenUseCase.deleteToken(tokenTemp,alert)
                .map(d->new Response())
                .switchIfEmpty(sendAlertToProviders (alert, templateSms))
                .then(Mono.empty());
    }
    private Mono<TemplateSms> template(Alert pAlert){
        return  Mono.just(pAlert)
                .filter(alert -> !alert.getMessage().isEmpty() )
                .map(alert -> TemplateSms.builder().bodyText(alert.getMessage()).build())
                .switchIfEmpty(templateGateway.findTemplateEmail(pAlert))
                .switchIfEmpty(Mono.error(new BusinessException(TEMPLATE_NOT_FOUND)));
    }


}
