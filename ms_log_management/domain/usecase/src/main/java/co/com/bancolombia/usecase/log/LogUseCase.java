package co.com.bancolombia.usecase.log;

import co.com.bancolombia.model.log.Log;
import co.com.bancolombia.model.log.QueryLog;
import co.com.bancolombia.model.log.gateways.LogGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class LogUseCase {
    private final LogGateway logGateway;

    public Mono<Void> saveLog(Log log){
        return logGateway.saveLog(log)
                .flatMap(logSaved -> Mono.empty());
    }

    public Mono<List<Log>> findLogsByDate(QueryLog queryLog){
        return differenceDate(queryLog)
                .filter(aBoolean -> aBoolean)
                .flatMap(aBoolean -> dataHot(queryLog))
                .switchIfEmpty(dataCold(queryLog));
    }

    private Mono<List<Log>> dataHot(QueryLog queryLog){
        return logGateway.findLog(queryLog);
    }

    private Mono<List<Log>> dataCold(QueryLog queryLog){
        return Mono.empty();
    }

    private Mono<Boolean> differenceDate(QueryLog queryLog){
        return Mono.just(queryLog.getReferenceDate().toLocalDate())
                .map(referenceDate->referenceDate.isBefore(queryLog.getEndDate().toLocalDate()));
    }
}
