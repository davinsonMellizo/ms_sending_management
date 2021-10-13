package co.com.bancolombia.model.log.gateways;

import co.com.bancolombia.model.log.Log;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface LogGateway {
    Mono<Log> saveLog(Log log);

    Flux<Log> listLogs(Map<String, String> headers);
}
