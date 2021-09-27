package co.com.bancolombia.config.model.contact.gateways;

import co.com.bancolombia.config.model.contact.Contact;
import co.com.bancolombia.config.model.message.Message;
import reactor.core.publisher.Flux;

public interface ContactGateway {
    Flux<Contact> findAllContactsByClient(Message message);
}
