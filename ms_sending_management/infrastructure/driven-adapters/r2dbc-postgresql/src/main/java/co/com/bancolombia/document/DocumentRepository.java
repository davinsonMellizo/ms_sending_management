package co.com.bancolombia.document;

import co.com.bancolombia.document.data.DocumentData;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface DocumentRepository extends ReactiveCrudRepository<DocumentData, String> {

    @Query("select * from document_type where id::text = :documentType or code = :documentType")
    Mono<DocumentData> getDocument(@Param("documentType") String documentType);

}
