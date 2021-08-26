package co.com.bancolombia.alert;

import co.com.bancolombia.alert.data.AlertData;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface AlertRepository extends ReactiveCrudRepository<AlertData, String> {

}
