package co.com.bancolombia.usecase.sendalert;

import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.events.gateways.CommandGateway;
import co.com.bancolombia.model.message.Message;
import co.com.bancolombia.model.message.Response;
import co.com.bancolombia.model.message.Sms;
import co.com.bancolombia.model.message.Template;
import co.com.bancolombia.usecase.log.LogUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RouterProviderPushUseCase {
    private final CommandGateway commandGateway;

    public Mono<Response> sendPush(Message message, Alert alert) {
        return Mono.just(Sms.builder()
                        .priority(1)
                        .to(message.getPhoneIndicator() + message.getPhone())
                        .template(new Template(message.getParameters(), "Compra"))
                        .text(alert.getMessage())
                        .provider("PUSH")
                        .documentNumber(Long.toString(message.getDocumentNumber()))
                        .documentType(Integer.toString(message.getDocumentType()))
                        .url(message.getUrl())
                        .build())
                .flatMap(commandGateway::sendCommandAlertSms)
                .doOnError(e -> Response.builder().code(1).description(e.getMessage()).build());
    }
}
