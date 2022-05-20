package co.com.bancolombia.consumer.adapter;

import co.com.bancolombia.commons.constants.PersonType;
import co.com.bancolombia.commons.enums.State;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.consumer.RestConsumer;
import co.com.bancolombia.consumer.config.ConsumerProperties;
import co.com.bancolombia.model.Request;
import co.com.bancolombia.model.client.Client;
import co.com.bancolombia.model.client.ResponseClient;
import co.com.bancolombia.model.client.gateways.ClientGateway;
import co.com.bancolombia.model.contact.Contact;
import co.com.bancolombia.model.contact.ResponseContacts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static co.com.bancolombia.commons.constants.ContactWay.*;
import static co.com.bancolombia.commons.enums.Header.*;
import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.INTERNAL_SERVER_ERROR;

@Component
@RequiredArgsConstructor
public class ClientAdapter implements ClientGateway {

    private final ConsumerProperties properties;
    private final RestConsumer<Request, ResponseClient> restConsumer;
    private final RestConsumer<RetrieveRequest, Response> restConsumerIs;
    private static final Integer NOT_FOUNT = 404;

    @Override
    public Mono<Boolean> matchClientWithBasicKit(Client client) {
        HashMap<String, String> headers = new HashMap();
        headers.put(DOCUMENT_TYPE, client.getDocumentType());
        headers.put(DOCUMENT_NUMBER, Long.toString(client.getDocumentNumber()));
        headers.put(ASSOCIATION_ORIGIN, client.getEnrollmentOrigin());

        return Mono.just(Request.builder().headers(headers).build())
                .flatMap(clientRequest -> restConsumer.post(properties.getResources().getBasicKit(),
                        clientRequest, ResponseClient.class, Error.class))
                .map(ResponseClient::getResponse);
    }

    @Override
    public Mono<ResponseContacts> retrieveAlertInformation(Client client) {
        var header = new HashMap<String,String>();
        header.put("message-id", properties.getMessageId());
        header.put("x-ibm-client-id", properties.getClientId());
        header.put("x-ibm-client-secret", properties.getClientSecret());

        return Mono.just(RetrieveRequest.builder().headers(header)
                        .data(DataRequest.builder().customerIdentification(CustomerIdentification
                                .builder()
                                .documentNumber(String.valueOf(client.getDocumentNumber()))
                                .documentType(client.getDocumentType())
                                .build()).build()).build())
                .flatMap(cli -> restConsumerIs.post(properties.getResources().getRetrieveInfo(),
                        cli, Response.class, Error.class))
                .filter(response -> Objects.nonNull(response.getData().getAlertIndicators()))
                .filter(response -> !(response.getData().getAlertIndicators().isEmpty()))
                .onErrorResume(e -> ((Error)e).getHttpsStatus().equals(NOT_FOUNT),e -> Mono.empty())
                .onErrorMap(throwable -> new TechnicalException(INTERNAL_SERVER_ERROR))
                .flatMap(response -> getResponse(response, client));
    }

    private Mono<ResponseContacts> getResponse(Response response, Client client) {
        LocalDate date = response.getData().getAlertIndicators().get(0).getLastDataModificationDate();
        return Mono.just(ResponseContacts.builder()
                .documentNumber(client.getDocumentNumber())
                .documentType(client.getDocumentType())
                .enrollmentOrigin(response.getData().getAlertIndicators().get(0).getAlertType())
                .status(State.ACTIVE.getValue())
                .modifiedDate(LocalDateTime.of(date,LocalTime.NOON))
                .contacts(getContacts(response))
                .build());
    }

    private List<Contact> getContacts(Response response) {
        List<Contact> contacts = new ArrayList<>();
        final var segment = "ALE".equals(response.getData().getAlertIndicators().get(0).getAlertType()) ?
                PersonType.PERSONAS : PersonType.INCLUSION;
        if (StringUtils.hasLength(response.getData().getAlertIndicators().get(0).getCustomerMobileNumber())) {
            contacts.add(buildContact(segment, response.getData().getAlertIndicators().get(0).getCustomerMobileNumber(),
                    SMS));
        }
        if (StringUtils.hasLength(response.getData().getAlertIndicators().get(0).getCustomerEmail())) {
            contacts.add(buildContact(segment, response.getData().getAlertIndicators().get(0).getCustomerEmail(),
                    MAIL));
        }
        if (StringUtils.hasLength(response.getData().getAlertIndicators().get(0).getPushActive())) {
            contacts.add(buildContact(segment, response.getData().getAlertIndicators().get(0).getPushActive(),
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