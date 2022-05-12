package co.com.bancolombia.consumer.adapter;

import co.com.bancolombia.commons.constants.PersonType;
import co.com.bancolombia.commons.enums.State;
import co.com.bancolombia.consumer.RestConsumer;
import co.com.bancolombia.consumer.config.ConsumerProperties;
import co.com.bancolombia.model.Request;
import co.com.bancolombia.model.client.Client;
import co.com.bancolombia.model.client.ResponseClient;
import co.com.bancolombia.model.client.gateways.ClientGateway;
import co.com.bancolombia.model.client.gateways.ResponsePostISeriesRetrieveAlert;
import co.com.bancolombia.model.contact.Contact;
import co.com.bancolombia.model.contact.ResponseContacts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static co.com.bancolombia.commons.constants.ContactWay.*;
import static co.com.bancolombia.commons.enums.Header.*;

@Component
@RequiredArgsConstructor
public class ClientAdapter implements ClientGateway {

    private final ConsumerProperties properties;
    private final RestConsumer<Request, ResponseClient> restConsumer;
    private final RestConsumer<Request, ResponsePostISeriesRetrieveAlert> restConsumerIs;

    @Override
    public Mono<Boolean> matchClientWithBasicKit(Client client) {
        HashMap<String, String> headers = new HashMap();
        headers.put(DOCUMENT_TYPE, client.getDocumentType());
        headers.put(DOCUMENT_NUMBER, Long.toString(client.getDocumentNumber()));
        headers.put(ASSOCIATION_ORIGIN, client.getEnrollmentOrigin());

        return Mono.just(Request.builder().headers(headers).build())
                .flatMap(clientRequest -> restConsumer.post(properties.getResources().getBasicKit(),
                        clientRequest, ResponseClient.class))
                .map(ResponseClient::getResponse);
    }

    @Override
    public Mono<ResponseContacts> retrieveAlertInformation(Client client) {
        HashMap header = new HashMap();
        header.put("x-ibm-client-id", properties.getClientId());
        header.put("x-ibm-client-secret", properties.getClientSecret());
        header.put("Message-Id", properties.getMessageId());
        header.put("accept", "application/vnd.bancolombia.v4+json");

        return Mono.just(RetrieveRequest.builder().headers(header)
                        .data(DataRequest.builder().customerIdentification(CustomerIdentification
                                .builder()
                                .documentNumber(String.valueOf(client.getDocumentNumber()))
                                .documentType(client.getDocumentType())
                                .build()).build()).build())
                .flatMap(cli -> restConsumerIs.post(properties.getResources().getRetrieveInfo(),
                        cli, ResponsePostISeriesRetrieveAlert.class))
                .map(response -> response.toBuilder().documentNumber(client.getDocumentNumber())
                        .documentType(client.getDocumentType()).build())
                .flatMap(this::getResponse);
    }

    private Mono<ResponseContacts> getResponse(ResponsePostISeriesRetrieveAlert response) {
        return Mono.just(ResponseContacts.builder()
                .documentNumber(response.getDocumentNumber())
                .documentType(response.getDocumentType())
                .enrollmentOrigin(response.getAlertType())
                .status(State.ACTIVE.getValue())
                .modifiedDate(response.getLastDataModificationDate())
                .contacts(getContacts(response))
                .build());
    }

    private List<Contact> getContacts(ResponsePostISeriesRetrieveAlert response) {
        List<Contact> contacts = new ArrayList<>();
        final var segment = "ALE".equals(response.getAlertType()) ? PersonType.PERSONAS : PersonType.INCLUSION;
        if (StringUtils.hasLength(response.getCustomerMobileNumber())) {
            contacts.add(buildContact(segment, response.getCustomerMobileNumber(),
                    SMS));
        } else if (StringUtils.hasLength(response.getCustomerEmail())) {
            contacts.add(buildContact(segment, response.getCustomerEmail(),
                    MAIL));
        } else if (StringUtils.hasLength(response.getPushActive())) {
            contacts.add(buildContact(segment, "no llega valor aún (revisar)",
                    PUSH));
        }
        return contacts;
    }

    private Contact buildContact(String channel, String value, String contactWay) {
        return Contact.builder()
                .segment(channel)
                .contactWay(contactWay)
                .value(value)
                .stateContact(State.ACTIVE.getValue())
                .build();
    }
}