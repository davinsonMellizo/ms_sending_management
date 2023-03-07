package co.com.bancolombia.model.log.gateways;

import co.com.bancolombia.model.log.Log;
import co.com.bancolombia.model.log.QueryLog;
import reactor.core.publisher.Mono;

import java.util.List;

public interface LogGateway {
    Mono<Log> saveLog(Log log);

    Mono<List<Log>> findLog(QueryLog queryLog);

}
