package co.com.bancolombia.log;

import co.com.bancolombia.AdapterOperations;
import co.com.bancolombia.log.data.LogData;
import co.com.bancolombia.log.data.LogMapper;
import co.com.bancolombia.model.log.Log;
import co.com.bancolombia.model.log.gateways.LogGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Repository
public class LogRepositoryImplement
        extends AdapterOperations<Log, LogData, String, LogRepository>
        implements LogGateway {

    @Autowired
    public LogRepositoryImplement(LogRepository repository, LogMapper mapper) {
        super(repository, mapper::toData, mapper::toEntity);
    }

    @Override
    public Mono<Log> saveLog(Log log) {
        return Mono.just(log.toBuilder().dateCreation(LocalDateTime.now()).build())
                .map(this::convertToData)
                .flatMap(repository::save)
                .thenReturn(log);
    }
}
