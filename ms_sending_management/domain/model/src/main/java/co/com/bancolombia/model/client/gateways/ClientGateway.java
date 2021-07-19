package co.com.bancolombia.model.client.gateways;

import co.com.bancolombia.model.client.Client;
import co.com.bancolombia.model.message.Message;
import co.com.bancolombia.model.response.StatusResponse;
import reactor.core.publisher.Mono;

public interface ClientGateway {

    Mono<Client> findClientByIdentification(Message message);

}
