package co.com.bancolombia.log.reader;

import co.com.bancolombia.log.data.LogData;
import co.com.bancolombia.model.log.Log;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

public interface LogRepositoryReader extends ReactiveCrudRepository<LogData, String> {

    @Query("SELECT * FROM log " +
            "where (document_number::text like :documentNumber||'%' or document_number is null) and " +
            "(document_type like :documentType||'%' or document_type is null ) and " +
            "(contact like :contact||'%' or contact is null)  and " +
            "(consumer like :consumer||'%' or consumer is null)  and " +
            "(provider like :provider||'%' or provider is null)  and " +
            "date_creation BETWEEN :startDate AND :endDate")
    Flux<Log> findAllLogByFilters(@Param("documentNumber") String documentNumber,
                                  @Param("documentType") String documentType,
                                  @Param("contact") String contact,
                                  @Param("consumer") String consumer,
                                  @Param("provider") String provider,
                                  @Param("startDate") LocalDateTime startDate,
                                  @Param("endDate") LocalDateTime endDate);

}
