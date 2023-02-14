package co.com.bancolombia.alert;

import co.com.bancolombia.alert.data.AlertData;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AlertRepository extends ReactiveCrudRepository<AlertData, String> {
    @Query("select * from alert a where a.basic_kit = true and a.id_state = 0")
    Flux<AlertData> findAlertsKitBasic();

    @Query("select * from alert_view where id = $1 limit 1")
    Mono<AlertData> findAlertById(String id);

    @Query("select * from alert_view where id_consumer = $1 and id_transaction = $2")
    Flux<AlertData> findAlertByTrx(String idConsumer, String idTransaction);

}
