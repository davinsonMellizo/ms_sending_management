package co.com.bancolombia.usecase.sendalert.validations;

import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.alert.gateways.AlertGateway;
import co.com.bancolombia.model.client.gateways.ClientGateway;
import co.com.bancolombia.model.consumer.Consumer;
import co.com.bancolombia.model.contact.Contact;
import co.com.bancolombia.model.contact.gateways.ContactGateway;
import co.com.bancolombia.model.message.Message;
import co.com.bancolombia.model.message.Response;
import co.com.bancolombia.usecase.log.LogUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.constants.State.ACTIVE;
import static co.com.bancolombia.commons.constants.TypeLogSend.SEND_220;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.INVALID_CONTACTS;

@RequiredArgsConstructor
public class ValidateContactUseCase {
    private final ContactGateway contactGateway;
    private final LogUseCase logUseCase;
    public Mono<Message> validateDataContact(Message message, Consumer consumer) {
        return contactGateway.findAllContactsByClient(message.toBuilder().consumer(consumer.getSegment()).build())
                .collectMap(Contact::getContactMedium)
                .map(contacts -> message.toBuilder()
                        .phone(contacts.get("SMS") != null ? contacts.get("SMS").getValue() : "")
                        .push(contacts.get("PUSH") != null ? contacts.get("PUSH").getIdState() == ACTIVE ? true : false : false)
                        .mail(contacts.get("MAIL") != null ? contacts.get("MAIL").getValue() : "").build())
                .switchIfEmpty(Mono.error(new BusinessException(INVALID_CONTACTS)))
                .onErrorResume(BusinessException.class, e -> logUseCase.sendLogError(message, SEND_220,
                        new Response(1, e.getBusinessErrorMessage().getMessage())));
    }
}
