package co.com.bancolombia.alert;

import co.com.bancolombia.alert.data.AlertData;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AlertRepository extends ReactiveCrudRepository<AlertData, Integer> {

    @Query("select c.* , e.code as enrollment_contact, m.code as contact_medium, s.name as state " +
            "from contact c " +
            "inner join enrollment_contact e on c.id_enrollment_contact = e.id " +
            "inner join contact_medium m on c.id_contact_medium = m.id " +
            "inner join state s on c.id_state = s.id " +
            "where c.document_number::int8 = :documentNumber and c.document_type::int2 = :documentType")
    Flux<AlertData> findAllContactsByClient(@Param("documentNumber") Long documentNumber,
                                            @Param("documentType") Integer documentType);

    @Query("select c.* , e.code as enrollment_contact, m.code as contact_medium, s.name as state " +
            "FROM contact c " +
            "inner join enrollment_contact e on c.id_enrollment_contact = e.id " +
            "inner join contact_medium m on c.id_contact_medium = m.id " +
            "inner join state s on c.id_state = s.id " +
            "where c.document_number::int8 = :documentNumber and c.document_type::int2 = :documentType " +
            "and m.code = :contactMedium and e.code = :enrollmentContact")
    Mono<AlertData> findContact(@Param("documentNumber") Long documentNumber,
                                @Param("documentType") Integer documentType,
                                @Param("contactMedium") String contactMedium,
                                @Param("enrollmentContact") String enrollmentContact);
}
