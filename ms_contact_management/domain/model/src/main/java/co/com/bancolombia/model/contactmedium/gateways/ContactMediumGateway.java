package co.com.bancolombia.model.contactmedium.gateways;

import co.com.bancolombia.model.contactmedium.ContactMedium;
import reactor.core.publisher.Mono;

public interface ContactMediumGateway {
    Mono<ContactMedium> findContactMediumByCode(String code);
}
