package co.com.bancolombia.client;

import co.com.bancolombia.client.data.ClientData;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ClientRepository extends ReactiveCrudRepository<ClientData, Long> {
    @Query("select * from client " +
            "inner join document_type d on d.id = c.id_document_type " +
            "where document_number = $1 and (d.id::text = $2 or d.code = $2)")
    Mono<ClientData> findClientByIdentification(Long documentNumber, Integer documentType);
}
