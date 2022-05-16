package co.com.bancolombia.document;

import co.com.bancolombia.AdapterOperations;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.document.data.DocumentData;
import co.com.bancolombia.document.data.DocumentMapper;
import co.com.bancolombia.model.document.Document;
import co.com.bancolombia.model.document.gateways.DocumentGateway;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.FIND_DOCUMENT_ERROR;

@Repository
public class DocumentRepositoryImplement
        extends AdapterOperations<Document, DocumentData, String, DocumentRepository, DocumentRepository>
        implements DocumentGateway {

    public DocumentRepositoryImplement(DocumentRepository repository, DocumentMapper mapper) {
        super(repository, repository, null, mapper::toEntity);
    }

    @Override
    public Mono<Document> getDocument(String typeDocument) {
        return repository.getDocument(typeDocument)
                .map(this::convertToEntity)
                .onErrorMap(e -> new TechnicalException(e, FIND_DOCUMENT_ERROR));
    }
}
