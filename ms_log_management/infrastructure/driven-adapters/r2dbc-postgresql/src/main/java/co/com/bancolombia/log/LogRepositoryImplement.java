package co.com.bancolombia.log;

import co.com.bancolombia.AdapterOperations;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.log.data.LogData;
import co.com.bancolombia.log.data.LogMapper;
import co.com.bancolombia.log.reader.LogRepositoryReader;
import co.com.bancolombia.log.writer.LogRepository;
import co.com.bancolombia.model.log.Log;
import co.com.bancolombia.model.log.QueryLog;
import co.com.bancolombia.model.log.gateways.LogGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.FIND_LOG_ERROR;
import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.SAVE_LOG_ERROR;

@Repository
public class LogRepositoryImplement
        extends AdapterOperations<Log, LogData, String, LogRepository, LogRepositoryReader>
        implements LogGateway {

    @Autowired
    public LogRepositoryImplement(LogRepository logRepository, LogRepositoryReader repositoryReader, LogMapper mapper) {
        super(logRepository, repositoryReader, mapper::toData, null);
    }

    @Override
    public Mono<Log> saveLog(Log log) {
        return Mono.just(log.toBuilder().dateCreation(LocalDateTime.now()).build())
                .map(this::convertToData)
                .flatMap(repository::save)
                .thenReturn(log)
                .onErrorMap(e -> new TechnicalException(e, SAVE_LOG_ERROR));
    }

    @Override
    public Mono<List<Log>> findLog(QueryLog queryLog) {
        return repositoryRead.findAllLogByFilters(queryLog.getDocumentNumber(), queryLog.getDocumentType(),
                queryLog.getContactValue(), queryLog.getConsumer(), queryLog.getProvider(), queryLog.getStartDate(),
                queryLog.getEndDate())
                .collectList()
                .onErrorMap(e -> new TechnicalException(e, FIND_LOG_ERROR));
    }

}
