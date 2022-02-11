package co.com.bancolombia.model.document.gateways;

import co.com.bancolombia.model.document.Document;
import reactor.core.publisher.Mono;

public interface DocumentGateway {
    Mono<Document> getDocument(String typeDocument);
}
