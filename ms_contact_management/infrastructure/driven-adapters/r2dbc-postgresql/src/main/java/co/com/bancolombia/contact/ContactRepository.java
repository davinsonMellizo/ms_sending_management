package co.com.bancolombia.contact;

import co.com.bancolombia.contact.data.ContactData;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ContactRepository extends ReactiveCrudRepository<ContactData, Integer> {

    @Query("select c.value, c.created_date, c.modified_date, c.segment, " +
            "m.code as contact_medium, s.name as state " +
            "from contact c " +
            "inner join document_type d on d.id = c.id_document_type "+
            "inner join contact_medium m on c.id_contact_medium = m.id " +
            "inner join state s on c.id_state = s.id " +
            "where c.document_number::int8 = :documentNumber and (d.id::text = :documentType or d.code = :documentType)")
    Flux<ContactData> findAllContactsByClient(@Param("documentNumber") Long documentNumber,
                                              @Param("documentType") String documentType);

    @Query("select c.* , c.segment as consumer, m.id as id_contact_medium, s.id as id_state " +
            "FROM contact c " +
            "inner join document_type d on d.id = c.id_document_type "+
            "inner join contact_medium m on c.id_contact_medium = m.id " +
            "inner join state s on c.id_state = s.id " +
            "where c.document_number::int8 = :documentNumber and m.code = :contactMedium and c.segment = :segment " +
            "and (d.id::text = :documentType or d.code = :documentType)")
    Mono<ContactData> findContact(@Param("documentNumber") Long documentNumber,
                                  @Param("documentType") String documentType,
                                  @Param("contactMedium") String contactMedium,
                                  @Param("segment") String segment);

}
