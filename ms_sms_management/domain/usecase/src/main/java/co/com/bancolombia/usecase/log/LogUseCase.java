package co.com.bancolombia.usecase.log;

import co.com.bancolombia.model.log.Log;
import co.com.bancolombia.model.log.gateways.LogGateway;
import co.com.bancolombia.model.message.Alert;
import co.com.bancolombia.model.message.Response;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.constants.TypeLogSend.SEND_230;

@RequiredArgsConstructor
public class LogUseCase {
    private final LogGateway logGateway;
    private static final int CODE_RESPONSE_200 = 200;
    private static final int CODE_RESPONSE_202 = 202;

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
                .filter(log -> (response.getCode()!= CODE_RESPONSE_200) && (response.getCode()!= CODE_RESPONSE_202))
                .flatMap(log -> Mono.error(new Throwable(response.getCode().toString())));
    }
}
