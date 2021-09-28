package co.com.bancolombia.model.client.gateways;

import co.com.bancolombia.model.client.Client;
import co.com.bancolombia.model.message.Message;
import reactor.core.publisher.Mono;

public interface ClientGateway {

    Mono<Client> findClientByIdentification(Message message);

}
