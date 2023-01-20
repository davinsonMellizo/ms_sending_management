package co.com.bancolombia.usecase.sendalert;

import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.alert.gateways.AlertGateway;
import co.com.bancolombia.model.client.Enrol;
import co.com.bancolombia.model.consumer.Consumer;
import co.com.bancolombia.model.consumer.gateways.ConsumerGateway;
import co.com.bancolombia.model.contact.Contact;
import co.com.bancolombia.model.contact.gateways.ContactGateway;
import co.com.bancolombia.model.response.StatusResponse;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static co.com.bancolombia.commons.constants.ContactWay.MAIL;
import static co.com.bancolombia.commons.constants.ContactWay.SMS;

@RequiredArgsConstructor
public class SendAlertUseCase {

    private final AlertGateway alertGateway;
    private final ContactGateway contactGateway;
    private final ConsumerGateway consumerGateway;


    public Mono<StatusResponse<Enrol>> sendAlertCreate(Enrol enrol, StatusResponse<Enrol> response, boolean isIseries){
        var parameters = new HashMap<String, String>();
        return Mono.just(isIseries)
                .filter(isiries -> !isiries)
                .flatMap(aBoolean -> sendAlertToSending(enrol, response.getActual().getContactData(), "0060", parameters))
                .then(Mono.just(response));
    }

    public Mono<StatusResponse<Enrol>> sendAlertUpdate(Enrol enrol, StatusResponse<Enrol> response, boolean isIseries){
        var parameters = new HashMap<String, String>();
        return Mono.just(enrol.getContactData())
                .filter(contacts -> !contacts.isEmpty())
                .filter(contacts -> !isIseries)
                .map(contacts -> enrol.getClient().getConsumerCode())
                .flatMap(consumerGateway::findConsumerById)
                .map(Consumer::getSegment)
                .flatMap(segment -> contactGateway.contactsByClientAndSegment(enrol.getClient(), segment))
                .flatMap(contacts ->  sendAlertToSending(enrol, contacts, "0050", parameters))
                .concatWith(sendAlertToSending(enrol, response.getBefore().getContactData(), "0050", parameters))
                .then(Mono.just(response));
    }

    public Mono<Enrol> sendAlertToSending(Enrol enrol, List<Contact> contacts, String alert, Map<String, String> params){
        return getPreferences(contacts)
                .map(preferences -> Alert.builder()
                        .operation(5)
                        .alert(alert)
                        .documentNumber(enrol.getClient().getDocumentNumber())
                        .documentType(Integer.parseInt(enrol.getClient().getDocumentType()))
                        .mail(getValueContact(contacts, MAIL))
                        .phone(getValueContact(contacts, SMS))
                        .preferences(preferences)
                        .parameters(params)
                        .build())
                .flatMap(alertGateway::sendAlert)
                .thenReturn(enrol);
    }

    private Mono<List<String>> getPreferences(List<Contact> contacts){
        return Flux.fromIterable(contacts)
                .map(this::getName)
                .collectList();
    }

    private String getValueContact(List<Contact> contacts, String medium){
        return contacts.stream()
                .filter(contact -> getName(contact).equals(medium))
                .map(Contact::getValue)
                .collect(Collectors.joining());
    }

    private  String getName(Contact contact){
        return contact.getContactWayName() != null ? contact.getContactWayName() : contact.getContactWay();
    }


}
