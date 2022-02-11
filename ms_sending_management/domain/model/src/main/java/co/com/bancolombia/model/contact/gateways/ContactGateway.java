package co.com.bancolombia.model.contact.gateways;

import co.com.bancolombia.model.contact.Contact;
import co.com.bancolombia.model.message.Message;
import reactor.core.publisher.Flux;

public interface ContactGateway {
    Flux<Contact> findAllContactsByClient(Message message);
}
