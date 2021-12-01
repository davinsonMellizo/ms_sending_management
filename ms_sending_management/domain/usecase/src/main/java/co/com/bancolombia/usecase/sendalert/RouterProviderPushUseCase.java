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

import java.util.UUID;

import static co.com.bancolombia.commons.constants.TypeLogSend.SEND_220;

@RequiredArgsConstructor
public class RouterProviderPushUseCase {
    private final CommandGateway commandGateway;
    private final LogUseCase logUseCase;

    public Mono<Response> sendPush(Message message, Alert alert) {
        Response response =  new Response(0, "Success");
        return logUseCase.sendLogPush(message, alert, SEND_220, alert.getMessage(),response)
                .map(responseLog -> Sms.builder()
                        .logKey(message.getLogKey())
                        .priority(1)
                        .to(message.getPhoneIndicator() + message.getPhone())
                        .template(new Template(message.getParameters(), alert.getTemplateName()))
                        .text(alert.getMessage())
                        .provider("PUSH")
                        .documentNumber(Long.toString(message.getDocumentNumber()))
                        .documentType(Integer.toString(message.getDocumentType()))
                        .url(message.getUrl())
                        .build())
                .flatMap(commandGateway::sendCommandAlertSms)
                .doOnError(e -> Response.builder().code(1).description(e.getMessage()).build())
                .thenReturn(response);
    }
}
