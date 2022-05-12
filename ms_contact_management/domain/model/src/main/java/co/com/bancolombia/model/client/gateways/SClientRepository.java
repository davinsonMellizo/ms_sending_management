package co.com.bancolombia.model.client.gateways;

import co.com.bancolombia.model.client.Client;
import co.com.bancolombia.model.response.StatusResponse;
import reactor.core.publisher.Mono;

public interface SClientRepository {
    Client saveSincrono(Client client);

}
