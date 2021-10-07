package co.com.bancolombia.alert;

import co.com.bancolombia.alert.data.AlertData;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface AlertRepository extends ReactiveCrudRepository<AlertData, String> {
    @Query("select * from alert a where a.basic_kit = true and a.id_state = 0")
    Flux<AlertData> findAlertsKitBasic();

}
