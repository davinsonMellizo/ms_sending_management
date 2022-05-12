package co.com.bancolombia.log;

import co.com.bancolombia.AdapterOperations;
import co.com.bancolombia.log.data.LogData;
import co.com.bancolombia.log.data.LogMapper;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.model.log.Log;
import co.com.bancolombia.model.log.gateways.LogGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.SAVE_LOG_ERROR;

@Repository
public class LogRepositoryImplement
        extends AdapterOperations<Log, LogData, String, LogRepository>
        implements LogGateway {

    private final R2dbcEntityTemplate entityTemplate;

    @Autowired
    public LogRepositoryImplement(LogRepository repository, LogMapper mapper, R2dbcEntityTemplate entityTemplate) {
        super(repository, mapper::toData, null);
        this.entityTemplate = entityTemplate;
    }

    @Override
    public Mono<Log> saveLog(Log log) {
        return Mono.just(log.toBuilder().dateCreation(LocalDateTime.now()).build())
                .map(this::convertToData)
                .flatMap(repository::save)
                .thenReturn(log)
                .onErrorMap(e -> new TechnicalException(e, SAVE_LOG_ERROR));
    }

}
