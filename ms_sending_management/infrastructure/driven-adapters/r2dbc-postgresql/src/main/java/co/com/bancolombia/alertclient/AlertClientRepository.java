package co.com.bancolombia.alertclient;

import co.com.bancolombia.alertclient.data.AlertClientData;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface AlertClientRepository extends ReactiveCrudRepository<AlertClientData, String> {

    @Modifying
    @Query("update alert_client " +
            "set number_operations = $1, amount_enable = $2 " +
            "where id_alert = $3 and document_number= $4 and id_document_type = $5")
    Mono<Integer> updateAlertClient(Integer numberOperations, Long amount, String idAlert,
                                    Long documentNumber, Integer documentType);

    @Modifying
    @Query("update alert_client " +
            "set accumulated_operations = $1, accumulated_amount = $2, transaction_date = $6 " +
            "where id_alert = $3 and document_number= $4 and id_document_type = $5")
    Mono<Integer> accumulate(Integer numberOperations, Long amount, String idAlert,
                             Long documentNumber, Integer documentType, LocalDateTime dateTransaction);

    @Modifying
    @Query("DELETE FROM alert_client where id_alert = $1 and document_number = $2 and id_document_type = $3")
    Mono<Integer> deleteAlertClient(String idAlert, Long documentNumber, Integer documentType);

    @Query("SELECT * FROM alert_client WHERE id_alert = $1 and document_number = $2 " +
            "and id_document_type = $3")
    Mono<AlertClientData> findAlertClient(String idAlert, Long documentNumber, Integer documentType);

    @Query("select ac.number_operations, ac.amount_enable, a.id as id_alert, a.description as alert_description, " +
            "ac.association_origin, ac.creation_user from alert a left join " +
            "(select * from alert_client where document_number = $1 and id_document_type = $2) ac " +
            "on a.id = ac.id_alert where a.visible_channel = true")
    Flux<AlertClientData> alertsClientVisibleChannelByClient(Long documentNumber, Integer documentType);

    @Query("select * from alert_client where document_number = $1 and id_document_type = $2")
    Flux<AlertClientData> findAlertsByClient(Long documentNumber, Integer documentType);
}
