package co.com.bancolombia.model.contactmedium.gateways;

import co.com.bancolombia.model.contactmedium.ContactMedium;
import co.com.bancolombia.model.state.State;
import reactor.core.publisher.Mono;

public interface ContactMediumGateway {
    Mono<ContactMedium> findContactMediumByCode(String code);
}
