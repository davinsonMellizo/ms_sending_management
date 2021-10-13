package co.com.bancolombia.log;

import co.com.bancolombia.AdapterOperations;
import co.com.bancolombia.commons.enums.Parameter;
import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.log.data.LogData;
import co.com.bancolombia.log.data.LogMapper;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.model.Filter;
import co.com.bancolombia.model.log.Log;
import co.com.bancolombia.model.log.gateways.LogGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Repository;
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
import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.BODY_MISSING_ERROR;
import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.SAVE_LOG_ERROR;
import static org.springframework.data.relational.core.query.Query.query;

@Repository
public class LogRepositoryImplement
        extends AdapterOperations<Log, LogData, String, LogRepository>
        implements LogGateway {

    private final R2dbcEntityTemplate entityTemplate;

    @Autowired
    public LogRepositoryImplement(LogRepository repository, LogMapper mapper, R2dbcEntityTemplate entityTemplate) {
        super(repository, mapper::toData, mapper::toEntity);
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

    @Override
    public Flux<Log> listLogs(Map<String, String> headers) {
        return Mono.just(buildQueryByFilters(headers))
                .onErrorMap(e-> new TechnicalException(BODY_MISSING_ERROR))
                .flatMapMany(query -> entityTemplate.select(query , LogData.class))
                .map(this::convertToEntity);
    }

    private Query buildQueryByFilters(Map<String, String> headers) {
        var criteria = Criteria.empty();

        criteria = (validateData(headers.get(DOCUMENT_NUMBER.getNameHeader())))
                ? criteria
                : criteria.and(DOCUMENT_NUMBER.getNameColumn()).is(Long.parseLong(headers.get(DOCUMENT_NUMBER.getNameHeader())));
        criteria = (validateData(headers.get(DOCUMENT_TYPE.getNameHeader())))
                ? criteria
                : criteria.and(DOCUMENT_TYPE.getNameColumn()).is(Integer.parseInt(headers.get(DOCUMENT_TYPE.getNameHeader())));
        criteria = (validateData(headers.get(START_DATE.getNameHeader())) && validateData(headers.get(END_DATE.getNameHeader())))
                ? criteria
                : criteria.and(START_DATE.getNameColumn()).between(LocalDateTime.parse(headers.get(START_DATE.getNameHeader())), LocalDateTime.parse(headers.get(END_DATE.getNameHeader())));

        return query(criteria);
    }


    private boolean validateData(String evaluateString) {
        return Objects.isNull(evaluateString) || evaluateString.isEmpty();
    }



}
