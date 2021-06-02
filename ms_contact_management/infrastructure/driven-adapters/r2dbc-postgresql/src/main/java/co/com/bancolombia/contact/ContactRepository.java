package co.com.bancolombia.contact;

import co.com.bancolombia.contact.data.ContactData;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ContactRepository extends ReactiveCrudRepository<ContactData, Integer> {

    @Query("select * from contact_view cv " +
            "where cv.document_number::int8 = :documentNumber and cv.document_type::int2 = :documentType")
    Flux<ContactData> findAllContactsByClient(@Param("documentNumber") Long documentNumber,
                                          @Param("documentType") Integer documentType);

    @Query("select * from contact_view cv " +
            "where cv.document_number::int8 = :documentNumber and cv.document_type::int2 = :documentType " +
            "and cv.contact_medium = :contactMedium and cv.enrollment_contact = :enrollmentContact")
    Mono<ContactData> findContact(@Param("documentNumber") Long documentNumber,
                                  @Param("documentType") Integer documentType,
                                  @Param("contactMedium") String contactMedium,
                                  @Param("enrollmentContact") String enrollmentContact);
}
