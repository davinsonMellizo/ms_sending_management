package co.com.bancolombia.usecase.sendalert;

import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.contact.Contact;
import co.com.bancolombia.model.contact.gateways.ContactGateway;
import co.com.bancolombia.model.message.Message;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Objects;

import static co.com.bancolombia.commons.constants.Medium.MAIL;
import static co.com.bancolombia.commons.constants.Medium.PUSH;
import static co.com.bancolombia.commons.constants.Medium.SMS;
import static co.com.bancolombia.commons.constants.State.ACTIVE;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.CLIENT_HAS_NO_CONTACTS;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.CLIENT_IDENTIFICATION_INVALID;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.CLIENT_INACTIVE;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.CLIENT_NOT_FOUND;
import static co.com.bancolombia.usecase.sendalert.commons.ValidateData.validateClient;

@RequiredArgsConstructor
public class ClientUseCase {
    private final ContactGateway contactGateway;
    private static final String REGEX_NUMBER = "^\\(\\+\\d{1,4}+\\)";
    private static final String REGEX_PREFIX = "\\)\\d++$";

    private String getPhone(Map<String, Contact> contacts){
        try {
            return contacts.get(SMS).getValue().split(REGEX_NUMBER)[1];
        } catch (ArrayIndexOutOfBoundsException | NullPointerException e){
            return  "";
        }

    }

    private String getPrefix(Map<String, Contact> contacts){
        try {
            return contacts.get(SMS).getValue().split(REGEX_PREFIX)[0].substring(1);
        } catch (StringIndexOutOfBoundsException | NullPointerException e){
            return  "";
        }

    }

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
                .switchIfEmpty(Mono.error(new BusinessException(CLIENT_HAS_NO_CONTACTS)))
                .doOnNext(contacts -> message.setPush(contacts.get(PUSH) != null))
                .doOnNext(contacts -> message.setMail(contacts.get(MAIL) != null ? contacts.get(MAIL).getValue() : ""))
                .doOnNext(contacts -> message.setPhone(getPhone(contacts)))
                .doOnNext(contacts -> message.setPhoneIndicator(getPrefix(contacts)))
                .thenReturn(message);
    }

    public Mono<Message> validateClientInformation(Message message) {
        return Mono.just(message)
                .filter(Message::getRetrieveInformation)
                .flatMap(this::validateDataContact)
                .switchIfEmpty(Mono.just(message));
    }
}
