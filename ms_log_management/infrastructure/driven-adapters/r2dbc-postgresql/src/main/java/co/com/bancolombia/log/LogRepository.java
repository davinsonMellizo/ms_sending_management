package co.com.bancolombia.log;

import co.com.bancolombia.log.data.LogData;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface LogRepository extends ReactiveCrudRepository<LogData, String> {


}
