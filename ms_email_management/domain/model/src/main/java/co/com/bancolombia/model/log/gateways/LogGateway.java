package co.com.bancolombia.model.log.gateways;

import co.com.bancolombia.model.log.Log;
import reactor.core.publisher.Mono;

public interface LogGateway {
    Mono<Log> putLogToSQS(Log log);
}
