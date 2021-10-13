package co.com.bancolombia.usecase.log;

import co.com.bancolombia.commons.enums.BusinessErrorMessage;
import co.com.bancolombia.commons.enums.Parameter;
import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.Filter;
import co.com.bancolombia.model.log.Log;
import co.com.bancolombia.model.log.gateways.LogGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

import static co.com.bancolombia.commons.enums.BusinessErrorMessage.INVALID_DATA;
import static co.com.bancolombia.commons.enums.Parameter.*;

@RequiredArgsConstructor
public class LogUseCase {
    private final LogGateway logGateway;
    public Mono<Void> saveLog(Log log){
        return logGateway.saveLog(log)
                .flatMap(logSaved -> Mono.empty());
    }

    public Mono<List<Log>> listLogs(Map<String, String> headers){
        return logGateway.listLogs(headers)
                .concatWith(listLogsHistory(headers))
                .collectList();
    }

    /*private Flux<Log> listLogsToday(Map<String, String> headers){

        return logGateway.listLogs(headers);
    }*/
    private Flux<Log> listLogsHistory(Map<String, String> headers){

        return Flux.empty();
    }

}
