package co.com.bancolombia.contact;

import co.com.bancolombia.contact.data.ContactData;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface ContactRepository extends ReactiveCrudRepository<ContactData, Integer> {

    //TODO: modificar la consulta
    @Query("select c.* , e.code as enrollment_contact, m.code as contact_medium, s.name as state " +
            "from contact c " +
            "inner join contact_medium m on c.id_contact_medium = m.id " +
            "inner join state s on c.id_state = s.id " +
            "where c.document_number::int8 = :documentNumber and c.document_type::int2 = :documentType " +
            "and c.segment like '%:consumer%'")
    Flux<ContactData> findAllContactsByClient(@Param("documentNumber") Long documentNumber,
                                              @Param("documentType") Integer documentType,
                                              @Param("consumer") String consumer);

}
