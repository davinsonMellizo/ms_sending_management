package co.com.bancolombia.client;

import co.com.bancolombia.AdapterOperations;
import co.com.bancolombia.client.data.ClientData;
import co.com.bancolombia.client.data.ClientMapper;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.drivenadapters.TimeFactory;
import co.com.bancolombia.model.client.Client;
import co.com.bancolombia.model.client.gateways.ClientRepository;
import co.com.bancolombia.model.response.StatusResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.constants.State.INACTIVE;
import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.*;

@Repository
public class ClientRepositoryImplement
        extends AdapterOperations<Client, ClientData, Integer, co.com.bancolombia.client.ClientRepository>
        implements ClientRepository {

    @Autowired
    private TimeFactory timeFactory;
    private static final int NUMBER_OF_ROWS = 1;

    @Autowired
    public ClientRepositoryImplement(co.com.bancolombia.client.ClientRepository repository, ClientMapper mapper) {
        super(repository, mapper::toData, mapper::toEntity);
    }


    @Override
    public Mono<Client> findClientByIdentification(Client client) {
        return repository.findClientByIdentification(client.getDocumentNumber(), client.getDocumentType())
                .map(this::convertToEntity)
                .onErrorMap(e -> new TechnicalException(e, FIND_CLIENT_ERROR));
    }

    @Override
    public Mono<Client> saveClient(Client client) {
        return Mono.just(client)
                .map(this::convertToData)
                .map(clientData -> clientData.toBuilder()
                        .createdDate(timeFactory.now())
                        .modifiedDate(timeFactory.now())
                        .build())
                .flatMap(this::saveData)
                .map(this::convertToEntity)
                .onErrorMap(e -> new TechnicalException(e, SAVE_CLIENT_ERROR));
    }

    @Override
    public Mono<StatusResponse<Client>> updateClient(StatusResponse<Client> statusResponse) {
        return Mono.just(statusResponse.getActual().toBuilder()
                .id(statusResponse.getBefore().getId())
                .creationUser(statusResponse.getBefore().getCreationUser())
                .createdDate(statusResponse.getBefore().getCreatedDate())
                .modifiedDate(timeFactory.now()).build())
                .map(this::convertToData)
                .flatMap(this::saveData)
                .map(this::convertToEntity)
                .map(clientUpdated -> statusResponse.toBuilder()
                        .actual(clientUpdated).build())
                .onErrorMap(e -> new TechnicalException(e, UPDATE_CLIENT_ERROR));
    }

    @Override
    public Mono<Client> deleteClient(Client client) {
        return repository.deleteClient(client.getDocumentNumber(), client.getDocumentType())
                .filter(rowsAffected -> rowsAffected == NUMBER_OF_ROWS)
                .map(integer -> client)
                .onErrorMap(e -> new TechnicalException(e, DELETE_CLIENT_ERROR));
    }

    @Override
    public Mono<Client> inactivateClient(Client client) {
        return Mono.just(client.toBuilder().idState(INACTIVE).modifiedDate(timeFactory.now()).build())
                .flatMap(this::save)
                .onErrorMap(e -> new TechnicalException(e, INACTIVE_CLIENT_ERROR));
    }
}
