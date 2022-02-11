package co.com.bancolombia.usecase.log;

import co.com.bancolombia.model.log.Log;
import co.com.bancolombia.model.log.gateways.LogGateway;
import co.com.bancolombia.model.message.Alert;
import co.com.bancolombia.model.message.Response;
import co.com.bancolombia.model.message.TemplateSms;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.constants.TypeLogSend.SEND_230;

@RequiredArgsConstructor
public class LogUseCase {
    private final LogGateway logGateway;

    public <T> Mono<T> sendLog(Alert alert, String medium, Response response) {
        return logGateway.putLogToSQS(Log.builder()
                .logKey(alert.getLogKey())
                .logType(SEND_230)
                .medium(medium)
                .contact(alert.getTo())
                .messageSent(alert.getMessage())
                .provider(alert.getProvider())
                .responseCode(response.getCode())
                .responseDescription(response.getDescription())
                .build())
                .filter(log -> response.getCode()!=200 && response.getCode()!=202)
                .flatMap(log -> Mono.error(new Throwable()));
    }
}
