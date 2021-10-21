package co.com.bancolombia.usecase.commons;

import co.com.bancolombia.model.alertclient.AlertClient;
import co.com.bancolombia.model.client.Client;
import co.com.bancolombia.model.contact.Contact;
import co.com.bancolombia.model.newness.Newness;
import lombok.experimental.UtilityClass;
import reactor.core.publisher.Mono;

@UtilityClass
public class FactoryLog {
    public  Mono<Newness> createLog(AlertClient alertClient) {
        return Mono.just(Newness.builder()
                .documentType(alertClient.getDocumentType())
                .documentNumber(alertClient.getDocumentNumber())
                .build());
    }
}
