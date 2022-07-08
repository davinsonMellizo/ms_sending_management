package co.com.bancolombia.contact.reader;

import co.com.bancolombia.contact.data.ContactData;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface ContactRepositoryReader extends ReactiveCrudRepository<ContactData, Integer> {

    @Query("select c.value, c.created_date, c.modified_date, c.segment, " +
            "m.code as contact_way, s.name as state_contact, c.environment_type " +
            "from contact c " +
            "inner join document_type d on d.id = c.id_document_type "+
            "inner join contact_medium m on c.id_contact_medium = m.id " +
            "inner join state s on c.id_state = s.id " +
            "where c.document_number::int8 = :documentNumber " +
            "and c.previous = false "+
            "and (d.id::text = :documentType or d.code = :documentType)")
    Flux<ContactData> findAllContactsByClient(@Param("documentNumber") Long documentNumber,
                                              @Param("documentType") String documentType);

    @Query("select c.value, c.created_date, c.modified_date, c.segment, " +
            "m.code as contact_way, s.name as state_contact, c.environment_type " +
            "from contact c " +
            "inner join document_type d on d.id = c.id_document_type "+
            "inner join contact_medium m on c.id_contact_medium = m.id " +
            "inner join state s on c.id_state = s.id " +
            "where c.document_number::int8 = :documentNumber and c.segment = :segment " +
            "and c.previous = false "+
            "and (d.id::text = :documentType or d.code = :documentType)")
    Flux<ContactData> contactsByClientAndSegment(@Param("documentNumber") Long documentNumber,
                                                 @Param("documentType") String documentType,
                                                 @Param("segment") String segment);

    @Query("select c.* , c.segment as consumer, m.id as contact_way, s.id as state_contact " +
            "FROM contact c " +
            "inner join document_type d on d.id = c.id_document_type "+
            "inner join contact_medium m on c.id_contact_medium = m.id " +
            "inner join state s on c.id_state = s.id " +
            "where c.document_number::int8 = :documentNumber and m.code = :contactMedium and c.segment = :segment " +
            "and (d.id::text = :documentType or d.code = :documentType)")
    Flux<ContactData> findContact(@Param("documentNumber") Long documentNumber,
                                  @Param("documentType") String documentType,
                                  @Param("contactMedium") String contactMedium,
                                  @Param("segment") String segment);

}
