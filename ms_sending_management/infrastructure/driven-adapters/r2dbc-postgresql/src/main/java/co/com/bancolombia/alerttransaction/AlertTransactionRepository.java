package co.com.bancolombia.alerttransaction;

import co.com.bancolombia.alerttransaction.data.AlertTransactionData;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AlertTransactionRepository extends ReactiveCrudRepository<AlertTransactionData, String> {

    @Modifying
    @Query("delete from alert_transaction where id_alert = $1 and id_consumer = $2 and id_transaction = $3")
    Mono<Integer> deleteAlertTransaction(String idAlert, String idConsumer, String idTransaction);

    @Query("select * from alert_transaction where id_consumer = $1 and id_transaction = $2")
    Flux<AlertTransactionData> findAllAlertTransaction(String idConsumer, String idTransaction);

    @Query("select * from alert_transaction where id_alert = $1")
    Flux<AlertTransactionData> findAllAlertTransaction(String idAlert);
}
