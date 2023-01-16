package co.com.bancolombia.log.writer;

import co.com.bancolombia.log.data.LogData;
import co.com.bancolombia.model.log.Log;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

public interface LogRepository extends ReactiveCrudRepository<LogData, String> {

}
