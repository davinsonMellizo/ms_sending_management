package co.com.bancolombia.model.client.gateways;

import co.com.bancolombia.model.client.Client;
import reactor.core.publisher.Mono;

public interface ClientGateway {

    Mono<Client> findClientByIdentification(Long documentNumber, Integer documentType);

}
