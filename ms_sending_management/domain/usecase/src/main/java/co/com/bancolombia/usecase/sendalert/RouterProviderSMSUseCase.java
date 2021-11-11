package co.com.bancolombia.usecase.sendalert;


import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.message.Message;
import co.com.bancolombia.model.message.SMS;
import co.com.bancolombia.model.message.SMSInalambria;
import co.com.bancolombia.model.prefix.gateways.PrefixRepository;
import co.com.bancolombia.model.provider.Provider;
import co.com.bancolombia.model.provider.gateways.ProviderGateway;
import co.com.bancolombia.model.providerservice.ProviderService;
import co.com.bancolombia.model.providerservice.gateways.ProviderServiceGateway;
import co.com.bancolombia.model.message.Response;
import co.com.bancolombia.usecase.log.LogUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.constants.Provider.INALAMBRIA;
import static co.com.bancolombia.commons.constants.Provider.MASIVIAN;
import static co.com.bancolombia.commons.constants.TypeLogSend.SEND_220;
import static co.com.bancolombia.commons.constants.TypeLogSend.SEND_230;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.INVALID_CONTACT;
import static co.com.bancolombia.usecase.sendalert.commons.ValidateData.isValidMobile;

@RequiredArgsConstructor
public class RouterProviderSMSUseCase {
    private final PrefixRepository prefixRepository;
    private final LogUseCase logUseCase;
    private final ProviderGateway providerGateway;
    private final ProviderServiceGateway providerServiceGateway;

    public Mono<Response> routingAlertsSMS(Message message, Alert alert){
        return Mono.just(message)
                .filter(isValidMobile)
                .map(message1 -> message.getPhone().substring(0,3))
                .flatMap(prefixRepository::findPrefix)
                .switchIfEmpty(logUseCase.sendLogSMS(message, alert, SEND_220,
                        message.getParameters().get(0).getValue(), new Response( 1,INVALID_CONTACT)))
                .map(prefix -> alert.getIdProviderSms())
                .flatMap(providerServiceGateway::findProviderService)
                .map(ProviderService::getIdProvider)
                .flatMap(providerGateway::findProviderById)
                .flatMap(provider -> sendAlertMobile(alert, message, provider))
                .doOnError(e -> logUseCase.sendLogSMS(message, alert, SEND_220,
                        message.getParameters().get(0).getValue(), new Response(1, e.getMessage())));
    }

    private Mono<Response> sendAlertMobile(Alert alert, Message message, Provider provider) {
        return
                sendSMSInalambria(message, alert, provider)
                .concatWith(sendSMSMasivian(message, alert, provider)).last();
    }

    private Mono<Response> sendSMSInalambria(Message message, Alert alert, Provider pProvider){
        return Mono.just(pProvider)
                .filter(provider -> provider.getName().equals(INALAMBRIA))
                .map(provider ->  SMSInalambria.builder()
                        .MessageText(alert.getMessage())
                        .Devices(message.getPhoneIndicator()+message.getPhone())
                        .Type("1")
                        .build())
                .map(sms -> Response.builder().code(200).build())//TODO call to api Inalambria
                .doOnError(e -> Response.builder().code(1).description(e.getMessage()).build())
                .flatMap(response ->  logUseCase.sendLogSMS(message, alert, SEND_230,
                        message.getParameters().get(0).getValue(), response));
    }

    private Mono<Response> sendSMSMasivian(Message message, Alert alert, Provider pProvider){
        return Mono.just(pProvider)
                .filter(provider -> provider.getName().equals(MASIVIAN))
                .map(provider ->  SMS.builder()
                        .text(alert.getMessage()).Longmessage(true)
                        .Url(message.getUrl()).domainshorturl(false)
                        .To(message.getPhoneIndicator()+message.getPhone())
                        .IsPremium(false).IsFlash(false)
                        .build())
                .map(sms -> Response.builder().code(200).build())//TODO call to api Masivian
                .doOnError(e -> Response.builder().code(1).description(e.getMessage()).build())
                .flatMap(response ->  logUseCase.sendLogSMS(message, alert, SEND_230,
                        message.getParameters().get(0).getValue(), response));
    }

}
