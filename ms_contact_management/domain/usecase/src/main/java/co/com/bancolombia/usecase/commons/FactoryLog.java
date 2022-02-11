package co.com.bancolombia.usecase.commons;

import co.com.bancolombia.commons.enums.Response;
import co.com.bancolombia.model.client.Client;
import co.com.bancolombia.model.contact.Contact;
import co.com.bancolombia.model.newness.Newness;
import lombok.experimental.UtilityClass;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.constants.State.ACTIVE;

@UtilityClass
public class FactoryLog {
    public  Mono<Newness> createLog(Client client, String transaction) {
        return Mono.just(Newness.builder()
                .transactionDescription(transaction)
                .channelTransaction(client.getEnrollmentOrigin())
                .active(client.getIdState()==ACTIVE?true:false)
                .userCreation(client.getCreationUser())
                .documentType(Integer.parseInt(client.getDocumentType()))
                .documentNumber(client.getDocumentNumber())
                .responseCode(Response.SUCCESS_120.getCode())
                .responseDescription(Response.SUCCESS_120.getDescription())
                .build());
    }

    public Mono<Newness> createLog(Contact contact, String transaction) {
        return Mono.just(Newness.builder()
                .documentType(Integer.parseInt(contact.getDocumentType()))
                .documentNumber(contact.getDocumentNumber())
                .contact(contact.getValue())
                .transactionDescription(transaction)
                .active(Integer.parseInt(contact.getState())==ACTIVE?true:false)
                .responseCode(Response.SUCCESS_120.getCode())
                .responseDescription(Response.SUCCESS_120.getDescription())
                .build());
    }
}
