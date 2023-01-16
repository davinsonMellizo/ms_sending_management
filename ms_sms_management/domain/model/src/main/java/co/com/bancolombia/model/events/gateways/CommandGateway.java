package co.com.bancolombia.model.events.gateways;

import co.com.bancolombia.model.log.Log;
import reactor.core.publisher.Mono;

public interface CommandGateway {

    Mono<Log> sendCommanLogSms(Log log);
}
