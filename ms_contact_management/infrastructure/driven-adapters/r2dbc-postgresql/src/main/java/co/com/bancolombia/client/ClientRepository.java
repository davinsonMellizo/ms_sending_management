package co.com.bancolombia.client;

import co.com.bancolombia.client.data.ClientData;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface ClientRepository extends ReactiveCrudRepository<ClientData, Long> {
    @Query("select * from client " +
            "where document_number = $1 and document_type = $2")
    Mono<ClientData> findClientByIdentification(Long documentNumber, Integer documentType);

    @Modifying
    @Query("delete from client where document_number = $1 and document_type = $2")
    Mono<Integer> deleteClient(Long documentNumber, Integer documentType);

    @Modifying
    @Query("update client set key_mdm = $1, enrollment_origin = $2,modified_date = $3, id_state = $4 " +
            "where document_number = $5 and document_type = $6")
    Mono<Integer> updateClient(String keyMdm, String enrollmentOrigin, LocalDateTime modifiedDate,
                               Integer idState, Long documentNumber, Integer documentType);

}
