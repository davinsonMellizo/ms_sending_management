package co.com.bancolombia.client.writer;

import co.com.bancolombia.client.data.ClientData;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface IClientRepository extends ReactiveCrudRepository<ClientData, Integer> {

    @Query("DELETE FROM client " +
            "where document_number between $1 and $2")
    Mono<Integer> deleteClient(Long documentInit, Long documentEnd);

}
