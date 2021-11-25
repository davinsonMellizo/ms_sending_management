package co.com.bancolombia.usecase.sendalert;

import co.com.bancolombia.model.message.*;
import co.com.bancolombia.model.message.gateways.InalambriaGateway;
import co.com.bancolombia.model.message.gateways.MasivianGateway;
import co.com.bancolombia.model.message.gateways.PinpointGateway;
import co.com.bancolombia.model.message.gateways.PushGateway;
import co.com.bancolombia.usecase.log.LogUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.constants.Provider.*;
import static co.com.bancolombia.commons.constants.TypeLogSend.SEND_220;
import static co.com.bancolombia.commons.constants.TypeLogSend.SEND_230;
import static co.com.bancolombia.usecase.sendalert.commons.Medium.PUSH;
import static co.com.bancolombia.usecase.sendalert.commons.Medium.SMS;

@RequiredArgsConstructor
public class SendAlertUseCase {
    private final InalambriaGateway inalambriaGateway;
    private final MasivianGateway masivianGateway;
    private final PinpointGateway pinpointGateway;
    private final PushGateway pushGateway;
    private final LogUseCase logUseCase;

    public Mono<Void> sendAlertToProviders(Message message) {
        //TODO: llamar a pinpoint con la plantilla
        //TODO: reemplazar los parametros
        return sendPush(message)
                .concatWith(sendSMSInalambria(message))
                .concatWith(sendSMSMasivian(message))
                .thenEmpty(Mono.empty());
    }

    private Mono<Response> sendSMSInalambria(Message message) {
        return Mono.just(message.getProvider())
                .filter(provider -> provider.equalsIgnoreCase(INALAMBRIA))
                .map(provider -> SMSInalambria.builder()
                        .MessageText(message.getText())
                        .Devices(message.getTo())
                        .Type(1)
                        .build())
                .flatMap(inalambriaGateway::sendSMS)
                .doOnError(e -> Response.builder().code(1).description(e.getMessage()).build())
                .flatMap(response -> logUseCase.sendLog(message, SEND_230, SMS, response));
    }

    private Mono<Response> sendSMSMasivian(Message message) {
        return Mono.just(message.getProvider())
                .filter(provider -> provider.equalsIgnoreCase(MASIVIAN))
                .map(provider -> Sms.builder()
                        .text(message.getText()).Longmessage(true)
                        .Url(message.getUrl()).domainshorturl(false)
                        .To(message.getTo())
                        .IsPremium(false).IsFlash(false)
                        .build())
                .flatMap(masivianGateway::sendSMS)
                .doOnError(e -> Response.builder().code(1).description(e.getMessage()).build())
                .flatMap(response -> logUseCase.sendLog(message, SEND_230, SMS, response));
    }
    private Mono<Response> sendPush(Message message) {
        return Mono.just(message.getProvider())
                .filter(provider -> provider.equalsIgnoreCase(CONTACTABILIDAD))
                .map(response -> Push.builder()
                        .data(Push.Data.builder()
                                .sendMessage(Push.SendMessage.builder()
                                        .customerIdentification(Push.CustomerIdentification.builder()
                                                .customerDocumentNumber(message.getDocumentNumber())
                                                .customerDocumentType(message.getDocumentType())
                                                .build())
                                        .consumerId("AYN")
                                        .categoryId("1")
                                        .applicationCode(message.getEnrolClient())
                                        .message(message.getText())
                                        .build())
                                .build())
                        .build())
                .flatMap(pushGateway::sendPush)
                .doOnError(e -> Response.builder().code(1).description(e.getMessage()).build())
                .flatMap(response -> logUseCase.sendLog(message, SEND_230, PUSH, response));
    }
}
