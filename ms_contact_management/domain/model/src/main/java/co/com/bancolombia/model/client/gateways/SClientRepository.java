package co.com.bancolombia.model.client.gateways;

import co.com.bancolombia.model.client.Client;

public interface SClientRepository {
    Client saveSincrono(Client client);

}
