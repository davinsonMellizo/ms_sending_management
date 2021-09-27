package co.com.bancolombia.config.model.client.gateways;

import co.com.bancolombia.config.model.client.Client;
import co.com.bancolombia.config.model.message.Message;
import reactor.core.publisher.Mono;

public interface ClientGateway {

    Mono<Client> findClientByIdentification(Message message);

}
