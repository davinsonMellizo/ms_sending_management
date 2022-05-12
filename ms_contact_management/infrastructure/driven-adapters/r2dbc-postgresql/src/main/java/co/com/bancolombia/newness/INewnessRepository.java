package co.com.bancolombia.newness;

import co.com.bancolombia.newness.data.NewnessData;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface INewnessRepository extends ReactiveCrudRepository<NewnessData, Integer> {

    @Query("explain analyze INSERT INTO newness " +
            "(document_type, document_number, contact, channel_transaction, id_alert, description_alert, active, transaction_description, number_operations, amount_enable, user_creation, response_code, response_description, voucher, date_first_inscription, date_creation) " +
            "VALUES(0, 0, 'dd', 'dd', 'dd', 'dd', false, 'dd', 0, 0, 'dd', 'dd', 'dd', 0, '2021-02-16 10:10:25-05', '2021-02-16 10:10:25-05') ")
    Flux<String> insertNewness();

}
