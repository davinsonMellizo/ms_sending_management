package co.com.bancolombia.usecase.sendalert;

import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.contact.Contact;
import co.com.bancolombia.model.contact.gateways.ContactGateway;
import co.com.bancolombia.model.message.Message;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static co.com.bancolombia.commons.constants.Medium.*;
import static co.com.bancolombia.commons.constants.State.ACTIVE;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.*;

import static co.com.bancolombia.usecase.sendalert.commons.ValidateData.validateClient;

@RequiredArgsConstructor
public class ClientUseCase {
    private final ContactGateway contactGateway;

    private Mono<Message> validateDataContact(Message message) {
        return Mono.just(message)
                .filter(validateClient)
                .switchIfEmpty(Mono.error(new BusinessException(CLIENT_IDENTIFICATION_INVALID)))
                .flatMapMany(contactGateway::findAllContactsByClient)
                .switchIfEmpty(Mono.error(new BusinessException(CLIENT_NOT_FOUND)))
                .filter(contact -> contact.getStateClient().equals(ACTIVE))
                .switchIfEmpty(Mono.error(new BusinessException(CLIENT_INACTIVE)))
                .filter(contact -> Objects.nonNull(contact.getContactMedium()))
                .filter(contact -> contact.getConsumer().equals(message.getConsumer()))
                .filter(contact -> contact.getIdState().equals(ACTIVE))
                .filter(contact -> !contact.getPrevious())
                .collectMap(Contact::getContactMedium)
                .filter(contacts -> !contacts.isEmpty())
                .doOnNext(contacts -> message.setPhone(contacts.get(SMS) != null ? contacts.get(SMS).getValue() : ""))
                .doOnNext(contacts -> message.setPush(contacts.get(PUSH) != null))
                .doOnNext(contacts -> message.setMail(contacts.get(MAIL) != null ? contacts.get(MAIL).getValue() : ""))
                .switchIfEmpty(Mono.error(new BusinessException(CLIENT_HAS_NO_CONTACTS)))
                .thenReturn(message);
    }
    public Mono<Message> validateClientInformation(Message message){
        return Mono.just(message)
                .filter(Message::getRetrieveInformation)
                .flatMap(this::validateDataContact)
                .switchIfEmpty(Mono.just(message));
    }
}
