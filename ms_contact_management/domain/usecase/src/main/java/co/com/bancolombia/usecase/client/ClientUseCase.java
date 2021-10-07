package co.com.bancolombia.usecase.client;

import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.client.Client;
import co.com.bancolombia.model.client.Enrol;
import co.com.bancolombia.model.client.gateways.ClientGateway;
import co.com.bancolombia.model.client.gateways.ClientRepository;
import co.com.bancolombia.model.document.Document;
import co.com.bancolombia.model.document.gateways.DocumentGateway;
import co.com.bancolombia.model.response.StatusResponse;
import co.com.bancolombia.usecase.contact.ContactUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static co.com.bancolombia.commons.constants.State.ACTIVE;
import static co.com.bancolombia.commons.constants.State.INACTIVE;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.*;

@RequiredArgsConstructor
public class ClientUseCase {
    private final ClientRepository clientRepository;
    private final DocumentGateway documentGateway;
    private final ContactUseCase contactUseCase;
    private final ClientGateway clientGateway;

    public Mono<Client> findClientByIdentification(Client client) {
        return clientRepository.findClientByIdentification(client)
                .switchIfEmpty(Mono.error(new BusinessException(CLIENT_NOT_FOUND)));
    }

    public Mono<Client> inactivateClient(Client pClient){
        return findClientByIdentification(pClient)
                .filter(client -> client.getIdState()==ACTIVE)
                .flatMap(clientRepository::inactivateClient)
                .switchIfEmpty(Mono.error(new BusinessException(CLIENT_INACTIVE)));
    }

    public Mono<Client> saveClient(Enrol enrol) {
        return clientRepository.findClientByIdentification(enrol.getClient())
                .flatMap(this::validateClientStatus)
                .flatMap(client -> updateClient(enrol))
                .map(response -> response.getActual().getClient())
                .switchIfEmpty(createClientAndContacts(enrol));

    }
    private Mono<Client> createClientAndContacts(Enrol enrol){
        return documentGateway.getDocument(enrol.getClient().getDocumentType())
                .switchIfEmpty(Mono.error(new BusinessException(DOCUMENT_TYPE_NOT_FOUND)))
                .map(Document::getId)
                .map(documentType -> enrol.getClient().toBuilder().documentType(documentType).build())
                .flatMap(clientRepository::saveClient)
                .flatMap(clientGateway::matchClientWithBasicKit)
                .map(aBoolean -> enrol.getContacts())
                .flatMapMany(Flux::fromIterable)
                .flatMap(contactUseCase::saveContact)
                .then(Mono.just(enrol.getClient()));
    }

    private Mono<Client> validateClientStatus(Client pClient) {
        return Mono.just(pClient)
                .filter(client -> client.getIdState()==INACTIVE)
                .switchIfEmpty(Mono.error(new BusinessException(CLIENT_ACTIVE)));
    }

    public Mono<StatusResponse<Enrol>> updateClient(Enrol enrol) {
        return clientRepository.findClientByIdentification(enrol.getClient())
                .switchIfEmpty(Mono.error(new BusinessException(CLIENT_INACTIVE)))
                .flatMap(clientBefore -> buildResponse(clientBefore, enrol.getClient()))
                .flatMap(clientRepository::updateClient)
                .flatMap(response -> updateContacts(enrol, response));
    }

    private Mono<StatusResponse<Enrol>> updateContacts(Enrol enrol, StatusResponse<Client> responseClient){
        Enrol enrolActual = Enrol.builder().contacts(new ArrayList<>()).build();
        Enrol enrolBefore = Enrol.builder().contacts(new ArrayList<>()).build();
        StatusResponse<Enrol> responseUpdate = new StatusResponse<>("",enrolActual, enrolBefore);
        return Flux.fromIterable(enrol.getContacts())
                .flatMap(contactUseCase::updateContactRequest)
                .doOnNext(response -> responseUpdate.getActual().getContacts().add(response.getActual()))
                .doOnNext(response -> responseUpdate.getBefore().getContacts().add(response.getBefore()))
                .singleOrEmpty()
                .doOnNext(response -> responseUpdate.getActual().setClient(responseClient.getActual()))
                .doOnNext(response -> responseUpdate.getBefore().setClient(responseClient.getBefore()))
                .doOnNext(response -> responseUpdate.setDescription("Cliente actualizado exitosamente"))
                .map(response -> responseUpdate);
    }

    public Mono<Client> deleteClient(Client client) {
        return documentGateway.getDocument(client.getDocumentType())
                .switchIfEmpty(Mono.error(new BusinessException(DOCUMENT_TYPE_NOT_FOUND)))
                .map(Document::getId)
                .map(documentType -> client.toBuilder().documentType(documentType).build())
                .flatMap(clientRepository::deleteClient)
                .switchIfEmpty(Mono.error(new BusinessException(CLIENT_NOT_FOUND)));
    }

    private Mono<StatusResponse<Client>> buildResponse(Client before, Client actual) {
        return documentGateway.getDocument(actual.getDocumentType())
                .switchIfEmpty(Mono.error(new BusinessException(DOCUMENT_TYPE_NOT_FOUND)))
                .map(document -> StatusResponse.<Client>builder()
                        .before(before)
                        .actual(actual.toBuilder().documentType(document.getId()).build())
                        .build());
    }
}
