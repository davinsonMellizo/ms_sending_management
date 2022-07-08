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
import co.com.bancolombia.secretsmanager.SecretsManager;
import co.com.bancolombia.secretsmanager.SecretsNameStandard;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.UUID;
import java.util.Objects;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static co.com.bancolombia.commons.constants.ContactWay.*;
import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.INTERNAL_SERVER_ERROR;
import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.UNAUTHORIZED;

@Component
@RequiredArgsConstructor
public class ClientAdapter implements ClientGateway {

    private final ConsumerProperties properties;
    private final RestConsumer<RetrieveRequest, Response> restConsumerIs;
    private final SecretsManager secretsManager;
    private final SecretsNameStandard secretsNameStandard;
    private static final Integer NOT_FOUNT = 404;
    private static final Integer UNAUTHORIZED_CODE = 401;

    @Override
    public Mono<ResponseContacts> retrieveAlertInformation(Client client) {
        var header = new HashMap<String,String>();
        header.put("message-id", UUID.randomUUID().toString());

        return secretsNameStandard.secretForRetrieve()
                .flatMap(secretsName -> secretsManager.getSecret(secretsName, SecretRetrieve.class))
                .doOnNext(secret -> header.put("x-ibm-client-id", secret.getClientId()))
                .doOnNext(secret -> header.put("x-ibm-client-secret", secret.getClientSecret()))
                .map(s -> RetrieveRequest.builder().headers(header)
                        .data(DataRequest.builder().customerIdentification(CustomerIdentification
                                .builder()
                                .documentNumber(String.valueOf(client.getDocumentNumber()))
                                .documentType(client.getDocumentType())
                                .build()).build()).build())
                .flatMap(cli -> restConsumerIs.post(properties.getResources().getRetrieveInfo(),
                        cli, Response.class, ResponseError.class))
                .filter(response -> Objects.nonNull(response.getData().getAlertIndicators()))
                .filter(response -> !(response.getData().getAlertIndicators().isEmpty()))
                .onErrorMap(e -> !(e instanceof Error), e-> new TechnicalException(e.getMessage(),INTERNAL_SERVER_ERROR))
                .onErrorResume(e -> e instanceof Error && ((Error)e).getHttpsStatus().equals(NOT_FOUNT),e -> Mono.empty())
                .onErrorMap(e -> e instanceof Error && ((Error)e).getHttpsStatus().equals(UNAUTHORIZED_CODE),
                        e -> new TechnicalException(UNAUTHORIZED))
                .onErrorMap(Error.class, e  -> new TechnicalException(getErrorsDetail(e),INTERNAL_SERVER_ERROR))
                .flatMap(response -> getResponse(response, client));
    }
    private String getErrorsDetail(Error e){
        return ((ResponseError)e.getData()).getErrors().stream().map(ResponseError.Error::getDetail)
                .collect(Collectors.joining(","));
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