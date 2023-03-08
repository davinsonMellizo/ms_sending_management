package co.com.bancolombia.usecase.log;

import co.com.bancolombia.model.events.gateways.CommandGateway;
import co.com.bancolombia.model.log.Log;
import co.com.bancolombia.model.message.Alert;
import co.com.bancolombia.model.message.Response;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;


import static co.com.bancolombia.commons.constants.TypeLogSend.SEND_230;

@RequiredArgsConstructor
public class LogUseCase {
    private final CommandGateway commandGateway;

    public  Mono <Response> handlerLog(Alert alert, String medium,  Response response, Boolean sendLog){
        return Mono.just(sendLog)
                .filter(Boolean::booleanValue)
                .map(e-> Log.builder()
                        .logKey(alert.getTrackId())
                        .logType(SEND_230)
                        .medium(medium)
                        .contact(alert.getDestination().getPrefix()+alert.getDestination().getPhoneNumber())
                        .messageSent(alert.getMessage())
                        .provider(alert.getProvider())
                        .responseCode(response.getCode())
                        .responseDescription(response.getDescription())
                        .build())
                .flatMap(commandGateway::sendCommandLogSms)
                .map(log -> response)
                .switchIfEmpty(Mono.just(response));
    }

}
