package co.com.bancolombia.usecase.client;

import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.client.Client;
import co.com.bancolombia.model.client.gateways.ClientGateway;
import co.com.bancolombia.model.document.Document;
import co.com.bancolombia.model.document.gateways.DocumentGateway;
import co.com.bancolombia.model.response.StatusResponse;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.enums.BusinessErrorMessage.CLIENT_NOT_FOUND;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.DOCUMENT_TYPE_NOT_FOUND;

@RequiredArgsConstructor
public class ClientUseCase {
    private final ClientGateway clientGateway;
    private final DocumentGateway documentGateway;

    public Mono<Client> findClientByIdentification(Client client) {
        return clientGateway.findClientByIdentification(client)
                .switchIfEmpty(Mono.error(new BusinessException(CLIENT_NOT_FOUND)));
    }

    public Mono<Client> saveClient(Client client) {
        return documentGateway.getDocument(client.getDocumentType())
                .switchIfEmpty(Mono.error(new BusinessException(DOCUMENT_TYPE_NOT_FOUND)))
                .map(Document::getId)
                .map(documentType -> client.toBuilder().documentType(documentType).build())
                .flatMap(clientGateway::saveClient);
    }

    public Mono<StatusResponse<Client>> updateClient(Client client) {
        return clientGateway.findClientByIdentification(client)
                .switchIfEmpty(Mono.error(new BusinessException(CLIENT_NOT_FOUND)))
                .map(clientBefore -> buildResponse(clientBefore, client))
                .flatMap(clientGateway::updateClient);
    }

    public Mono<Client> deleteClient(Client client) {
        return documentGateway.getDocument(client.getDocumentType())
                .switchIfEmpty(Mono.error(new BusinessException(DOCUMENT_TYPE_NOT_FOUND)))
                .map(Document::getId)
                .map(documentType -> client.toBuilder().documentType(documentType).build())
                .flatMap(clientGateway::deleteClient)
                .switchIfEmpty(Mono.error(new BusinessException(CLIENT_NOT_FOUND)));
    }

    private StatusResponse<Client> buildResponse(Client before, Client actual) {
        return StatusResponse.<Client>builder()
                .before(before)
                .actual(actual.toBuilder().documentType(before.getDocumentType()).build())
                .build();
    }
}
