package co.com.bancolombia.usecase.log;

import co.com.bancolombia.model.log.Filters;
import co.com.bancolombia.model.log.Log;
import co.com.bancolombia.model.log.QueryLog;
import co.com.bancolombia.model.log.gateways.LogGateway;
import co.com.bancolombia.model.log.gateways.RetrieveLogsGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class LogUseCase {
    private final LogGateway logGateway;
    private final RetrieveLogsGateway retrieveLogsGateway;

    public Mono<Void> saveLog(Log log){
        return logGateway.saveLog(log)
                .flatMap(logSaved -> Mono.empty());
    }

    public Mono<List<Log>> findLogsByDate(QueryLog queryLog){
        return differenceDate(queryLog)
                .doOnNext(logs -> retrieveLogsS3(Filters.builder()
                        .startDate(LocalDateTime.now().minusDays(5))
                        .endDate(LocalDateTime.now())
                        .consumer("ALM").provider("MAS").contact("32178521")
                        .documentNumber("106177595").documentType("1")
                        .build()))
                .filter(aBoolean -> aBoolean)
                .flatMap(aBoolean -> dataHot(queryLog));
    }

    public Mono<Void> retrieveLogsS3(Filters filters){
        return retrieveLogsGateway.retrieveLogsS3(filters)
                .thenEmpty(Mono.empty());
    }

    private Mono<List<Log>> dataHot(QueryLog queryLog){
        return logGateway.findLog(queryLog);
    }

    private Mono<Boolean> differenceDate(QueryLog queryLog){
        return Mono.just(queryLog.getReferenceDate().toLocalDate())
                .map(referenceDate->referenceDate.isBefore(queryLog.getEndDate().toLocalDate()));
    }
}
