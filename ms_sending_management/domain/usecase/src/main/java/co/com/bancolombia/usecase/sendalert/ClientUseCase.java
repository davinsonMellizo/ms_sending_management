package co.com.bancolombia.usecase.sendalert;

import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.client.Client;
import co.com.bancolombia.model.client.gateways.ClientGateway;
import co.com.bancolombia.model.consumer.gateways.ConsumerGateway;
import co.com.bancolombia.model.contact.Contact;
import co.com.bancolombia.model.contact.gateways.ContactGateway;
import co.com.bancolombia.model.message.Message;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import static co.com.bancolombia.commons.constants.State.ACTIVE;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.CLIENT_INACTIVE;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.CLIENT_NOT_FOUND;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.CONSUMER_NOT_FOUND;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.INVALID_CONTACTS;

@RequiredArgsConstructor
public class ClientUseCase {
    private final ClientGateway clientGateway;
    private final ContactGateway contactGateway;
    private final ConsumerGateway consumerGateway;

    public Mono<Message> validateDataContact(Message message) {
        return consumerGateway.findConsumerById(message.getConsumer())
                .switchIfEmpty(Mono.error(new BusinessException(CONSUMER_NOT_FOUND)))
                .flatMapMany(consumer -> contactGateway.findAllContactsByClient(message.toBuilder()
                        .consumer(consumer.getSegment()).build()))
                .filter(contact -> contact.getIdState().equals(ACTIVE))
                .filter(contact -> !contact.getPrevious())
                .collectMap(Contact::getContactMedium)
                .filter(contacts -> !contacts.isEmpty())
                .map(contacts -> message.toBuilder()
                        .phone(contacts.get("SMS") != null ? contacts.get("SMS").getValue() : "")
                        .push(contacts.get("PUSH") != null)
                        .mail(contacts.get("MAIL") != null ? contacts.get("MAIL").getValue() : "").build())
                .switchIfEmpty(Mono.error(new BusinessException(INVALID_CONTACTS)));
    }
    public Mono<Client> validateClient(Message message){
        return Mono.just(message)
                .flatMap(message1 -> clientGateway.findClientByIdentification(message.getDocumentNumber(),
                        message.getDocumentType()))
                .switchIfEmpty(Mono.error(new BusinessException(CLIENT_NOT_FOUND)))
                .filter(client -> client.getIdState().equals(ACTIVE))
                .switchIfEmpty(Mono.error(new BusinessException(CLIENT_INACTIVE)));
    }
    public Mono<Message> validateClientInformation(Message message){
        return Mono.just(message)
                .filter(Message::getRetrieveInformation)
                .flatMap(this::validateDataContact)
                .zipWith(validateClient(message))
                .map(Tuple2::getT1)
                .switchIfEmpty(Mono.just(message));
    }
}
