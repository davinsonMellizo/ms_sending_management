package co.com.bancolombia.contact;

import co.com.bancolombia.contact.data.ContactData;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface ContactRepository extends ReactiveCrudRepository<ContactData, Integer> {

    @Query("select c.* " +
            "from contact_view c " +
            "where c.document_number::int8 = :documentNumber and c.id_document_type::int2 = :documentType " +
            "and c.consumer = :consumer")
    Flux<ContactData> findAllContactsByClient(@Param("documentNumber") Long documentNumber,
                                              @Param("documentType") Integer documentType,
                                              @Param("consumer") String consumer);

}
