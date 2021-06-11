package co.com.bancolombia.client;

import co.com.bancolombia.client.data.ClientData;
import co.com.bancolombia.client.data.ClientId;
import co.com.bancolombia.contact.data.ContactData;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ClientRepository extends ReactiveCrudRepository<ClientData, ClientId> {

}
