package co.com.bancolombia.usecase.sendalert;

import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.alert.AlertParameters;
import co.com.bancolombia.model.alert.ClientMessage;
import co.com.bancolombia.model.alert.Identification;
import co.com.bancolombia.model.alert.Mail;
import co.com.bancolombia.model.alert.Message;
import co.com.bancolombia.model.alert.Push;
import co.com.bancolombia.model.alert.Sms;
import co.com.bancolombia.model.alert.gateways.AlertGateway;
import co.com.bancolombia.model.client.Enrol;
import co.com.bancolombia.model.consumer.Consumer;
import co.com.bancolombia.model.consumer.gateways.ConsumerGateway;
import co.com.bancolombia.model.contact.Contact;
import co.com.bancolombia.model.contact.gateways.ContactGateway;
import co.com.bancolombia.model.response.StatusResponse;
import co.com.bancolombia.usecase.commons.BridgeContact;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static co.com.bancolombia.commons.constants.AlertParameters.ALERT;
import static co.com.bancolombia.commons.constants.AlertParameters.CHANNEL;
import static co.com.bancolombia.commons.constants.AlertParameters.DATE;
import static co.com.bancolombia.commons.constants.AlertParameters.HOUR;
import static co.com.bancolombia.commons.constants.ContactWay.MAIL;
import static co.com.bancolombia.commons.constants.ContactWay.PUSH;
import static co.com.bancolombia.commons.constants.ContactWay.SMS;
import static co.com.bancolombia.usecase.commons.BridgeContact.getName;
import static co.com.bancolombia.usecase.commons.BridgeContact.getValue;

@RequiredArgsConstructor
public class SendAlertUseCase {

    private final AlertGateway alertGateway;
    private final ContactGateway contactGateway;
    private final ConsumerGateway consumerGateway;


    public Mono<StatusResponse<Enrol>> sendAlertCreate(Enrol enrol, StatusResponse<Enrol> response, boolean isIseries){
        return Mono.just(enrol)
                .filter(enrol1 -> !isIseries)
                .flatMap(parameters -> sendAlertToSending(enrol, response.getActual().getContactData()))
                .onErrorResume(throwable -> Mono.just((enrol)))
                .then(Mono.just(response));
    }

    public Mono<StatusResponse<Enrol>> sendAlertUpdate(Enrol enrol, StatusResponse<Enrol> response, boolean isIseries){
        return Mono.just(enrol.getContactData())
                .filter(contacts -> !contacts.isEmpty())
                .filter(contacts -> !isIseries)
                .map(contacts -> enrol.getClient().getConsumerCode())
                .flatMap(consumerGateway::findConsumerById)
                .map(Consumer::getSegment)
                .flatMap(segment -> contactGateway.contactsByClientAndSegment(enrol.getClient(), segment))
                .flatMap(contacts ->  sendAlertToSending(enrol, contacts))
                .concatWith(validateBefore(enrol, response))
                .onErrorResume(throwable -> Mono.just((enrol)))
                .then(Mono.just(response));
    }

    private Mono<Enrol> validateBefore(Enrol enrol, StatusResponse<Enrol> response){
        return Flux.fromIterable(response.getBefore().getContactData())
                .filter(contact -> !getName(contact).equals(PUSH))
                .filter(contact -> !getValue(response.getActual().getContactData(), getName(contact)).equals(contact.getValue()))
                .collectList()
                .filter(contacts -> !contacts.isEmpty())
                .flatMap(contacts -> sendAlertToSending(enrol, contacts));
    }

    private Mono<Enrol> sendAlertToSending(Enrol enrol, List<Contact> contacts){
        LocalDate date = LocalDate.now();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date hour = new Date();
        var parameters = new HashMap<String, String>();
        parameters.put(CHANNEL, enrol.getClient().getConsumerCode());
        parameters.put(HOUR, dateFormat.format(hour));
        parameters.put(DATE, date.toString());
        return getPreferences(contacts)
                .map(preferences -> Alert.builder()
                        .retrieveInformation(false)
                        .client(ClientMessage.builder()
                                .identification(Identification.builder()
                                        .documentNumber(enrol.getClient().getDocumentNumber())
                                        .documentType(Integer.parseInt(enrol.getClient().getDocumentType()))
                                        .build())
                                .build())
                        .message(Message.builder()
                                .parameters(parameters)
                                .mail(Mail.builder().address(getValue(contacts, MAIL)).build())
                                .sms(Sms.builder().phone(getValue(contacts, SMS)).build())
                                .push(Push.builder().applicationCode(getValuePush(contacts, PUSH)).build())
                                .build())
                        .alertParameters(AlertParameters.builder()
                                .alert(ALERT)
                                .consumer(enrol.getClient().getConsumerCode())
                                .build())
                        .build())
                .flatMap(alertGateway::sendAlert)
                .thenReturn(enrol);
    }

    private Mono<List<String>> getPreferences(List<Contact> contacts){
        return Flux.fromIterable(contacts)
                .map(BridgeContact::getName)
                .collectList();
    }

    private String getValuePush(List<Contact> contacts, String medium){
        return contacts.stream()
                .filter(contact -> getName(contact).equals(medium))
                .map(Contact::getSegment)
                .collect(Collectors.joining());
    }

}
