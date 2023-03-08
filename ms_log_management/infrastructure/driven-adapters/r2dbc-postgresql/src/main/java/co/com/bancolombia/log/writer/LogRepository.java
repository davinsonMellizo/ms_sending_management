package co.com.bancolombia.log.writer;

import co.com.bancolombia.log.data.LogData;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface LogRepository extends ReactiveCrudRepository<LogData, String> {

}
