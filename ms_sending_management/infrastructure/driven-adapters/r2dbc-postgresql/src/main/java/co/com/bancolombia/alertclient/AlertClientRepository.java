package co.com.bancolombia.alertclient;

import co.com.bancolombia.alertclient.data.AlertClientData;
import co.com.bancolombia.model.alertclient.AlertClient;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.sql.In;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AlertClientRepository extends ReactiveCrudRepository<AlertClientData, String> {

    @Query("SELECT * FROM alert_client WHERE id_client = $1")
    Flux<AlertClientData> findAllAlertsByClient(Integer idClient);

    @Modifying
    @Query("update alert_client " +
            "set number_operations = $1, amount_enable = $2 " +
            "where id_alert = $3 and id_client = $4")
    Mono<Integer> updateAlertClient(Integer numberOperations, Long amount, String idAlert,
                                    Integer idClient);

    @Modifying
    @Query("DELETE FROM alert_client where id_alert = $1 and id_client = $2")
    Mono<Integer> deleteAlertClient(String idAlert, Integer idClient);

    @Query("SELECT * FROM alert_client WHERE id_alert = $1 and id_client = $2")
    Mono<AlertClientData> findAlertClient(String idAlert, Integer idClient);

    @Query("select ac.number_operations, ac.amount_enable, a.id as id_alert from alert a left join " +
            "(select * from alert_client where document_number::text = $1 and id_document_type::text = $2) ac " +
            "on a.id = ac.id_alert " +
            "where a.visible_channel = true")
    Flux<AlertClientData> findAlertClientByClient(String documentNumber, String documentType);
}
