package co.com.bancolombia.usecase.commons;

import co.com.bancolombia.model.client.Client;
import co.com.bancolombia.model.contact.Contact;
import co.com.bancolombia.model.newness.Newness;
import lombok.experimental.UtilityClass;
import reactor.core.publisher.Mono;

@UtilityClass
public class FactoryLog {
    public  Mono<Newness> createLog(Client client) {
        return Mono.just(Newness.builder()
                .documentType(Integer.parseInt(client.getDocumentType()))
                .documentNumber(client.getDocumentNumber())
                .build());
    }

    public Mono<Newness> createLog(Contact contact) {
        return Mono.just(Newness.builder()
                .documentType(Integer.parseInt(contact.getDocumentType()))
                .documentNumber(contact.getDocumentNumber())
                .build());
    }
}
