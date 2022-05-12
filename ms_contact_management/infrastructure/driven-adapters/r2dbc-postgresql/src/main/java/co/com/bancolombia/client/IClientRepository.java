package co.com.bancolombia.client;

import co.com.bancolombia.client.data.ClientData;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface IClientRepository extends ReactiveCrudRepository<ClientData, Integer> {

    @Query("select * from client c " +
            "inner join document_type d on d.id = c.id_document_type  " +
            "where document_number = $1 and (d.id::text = $2 or d.code = $2)")
    Mono<ClientData> findClientByIdentification(Long documentNumber, String documentType);

    @Query("DELETE FROM client " +
            "where document_number between $1 and $2")
    Mono<Integer> deleteClient(Long documentInit, Long documentEnd);

}
