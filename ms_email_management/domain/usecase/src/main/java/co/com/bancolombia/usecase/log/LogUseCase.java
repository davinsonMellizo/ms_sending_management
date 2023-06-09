package co.com.bancolombia.usecase.log;

import co.com.bancolombia.model.events.gateways.CommandGateway;
import co.com.bancolombia.model.log.Log;
import co.com.bancolombia.model.message.Alert;
import co.com.bancolombia.model.message.Response;
import co.com.bancolombia.model.message.TemplateEmail;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.constants.TypeLogSend.SEND_230;

@RequiredArgsConstructor
public class LogUseCase {
    private final CommandGateway commandGateway;
    private static final int CODE_RESPONSE_200 =200;
    private static final int CODE_RESPONSE_0 =0;

    public <T> Mono<T> sendLog(Alert alert, TemplateEmail template, String medium, Response response) {
        return commandGateway.sendCommanLogEmail(Log.builder()
                        .logKey(alert.getLogKey())
                        .logType(SEND_230)
                        .medium(medium)
                        .contact(alert.getDestination().getToAddress())
                        .provider(alert.getProvider())
                        .responseCode(response.getCode())
                        .responseDescription(response.getDescription())
                        .template(template.getBodyHtml())
                        .build())
                .filter(log -> response.getCode() != CODE_RESPONSE_200 )
                .filter(log -> response.getCode() != CODE_RESPONSE_0 )
                .flatMap(log -> Mono.error(new Throwable(response.getCode().toString())));
    }
}
