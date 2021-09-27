package co.com.bancolombia.client;

import co.com.bancolombia.client.data.ClientData;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface ClientRepository extends ReactiveCrudRepository<ClientData, Integer> {
    @Query("select * from client c " +
            "inner join document_type d on d.id = c.id_document_type  " +
            "where document_number = $1 and (d.id::text = $2 or d.code = $2)")
    Mono<ClientData> findClientByIdentification(Long documentNumber, String documentType);

    @Modifying
    @Query("delete from client where document_number = $1 and id_document_type::text = $2")
    Mono<Integer> deleteClient(Long documentNumber, String documentType);

    @Modifying
    @Query("update client set key_mdm = $1, enrollment_origin = $2,modified_date = $3, id_state = $4 " +
            "where document_number = $5 and document_type::text = $6")
    Mono<Integer> updateClient(String keyMdm, String enrollmentOrigin, LocalDateTime modifiedDate,
                               Integer idState, Long documentNumber, String documentType);

}
