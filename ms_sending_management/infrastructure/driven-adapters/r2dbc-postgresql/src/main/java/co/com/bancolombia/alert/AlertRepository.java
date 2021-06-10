package co.com.bancolombia.alert;

import co.com.bancolombia.alert.data.AlertData;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface AlertRepository extends ReactiveCrudRepository<AlertData, String> {

}
