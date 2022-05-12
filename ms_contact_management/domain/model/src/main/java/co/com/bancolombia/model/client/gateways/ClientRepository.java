package co.com.bancolombia.model.client.gateways;

import co.com.bancolombia.model.client.Client;
import co.com.bancolombia.model.response.StatusResponse;
import reactor.core.publisher.Mono;

public interface ClientRepository {
    Mono<Client> findClientByIdentification(Client client);

    Mono<Client> saveClient(Client client);

    Mono<StatusResponse<Client>> updateClient(StatusResponse<Client> statusResponse);

    Mono<Client> inactivateClient(Client client);

    Mono<Integer> deleteClient(Long documentInit, Long documentEnd);

}
