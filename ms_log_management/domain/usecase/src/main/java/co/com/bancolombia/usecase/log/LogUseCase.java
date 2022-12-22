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
        return retrieveLogsS3(Filters.builder()
                        .startDate(LocalDateTime.now().minusDays(35))
                        .endDate(LocalDateTime.now().minusDays(28))
                        .consumer(queryLog.getConsumer()).provider(queryLog.getProvider())
                        .contact(queryLog.getContactValue())
                        .documentNumber(queryLog.getDocumentNumber()).documentType(queryLog.getDocumentType())
                        .build())
                .flatMap(unused -> dataHot(queryLog));
    }

    public Mono<String> retrieveLogsS3(Filters filters){
        return retrieveLogsGateway.retrieveLogsS3(filters);
    }

    private Mono<List<Log>> dataHot(QueryLog queryLog){
        return logGateway.findLog(queryLog);
    }

}
