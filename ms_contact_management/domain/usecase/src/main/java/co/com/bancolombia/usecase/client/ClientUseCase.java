package co.com.bancolombia.usecase.client;

import co.com.bancolombia.commons.constants.PersonType;
import co.com.bancolombia.commons.enums.BusinessErrorMessage;
import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.bridge.Bridge;
import co.com.bancolombia.model.client.Client;
import co.com.bancolombia.model.client.Enrol;
import co.com.bancolombia.model.client.ResponseUpdateClient;
import co.com.bancolombia.model.client.gateways.ClientGateway;
import co.com.bancolombia.model.client.gateways.ClientRepository;
import co.com.bancolombia.model.consumer.gateways.ConsumerGateway;
import co.com.bancolombia.model.document.Document;
import co.com.bancolombia.model.document.gateways.DocumentGateway;
import co.com.bancolombia.model.events.gateways.CommandGateway;
import co.com.bancolombia.model.response.StatusResponse;
import co.com.bancolombia.usecase.contact.ContactUseCase;
import co.com.bancolombia.usecase.log.NewnessUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Objects;

import static co.com.bancolombia.commons.enums.State.ACTIVE;
import static co.com.bancolombia.commons.enums.State.INACTIVE;
import static co.com.bancolombia.commons.constants.Transaction.*;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.*;
import static co.com.bancolombia.usecase.commons.BridgeContact.*;

@RequiredArgsConstructor
public class ClientUseCase {

    private final ClientRepository clientRepository;
    private final DocumentGateway documentGateway;
    private final ContactUseCase contactUseCase;
    private final NewnessUseCase newnessUseCase;
    private final ClientGateway clientGateway;
    private final CommandGateway commandGateway;
    private final ConsumerGateway consumerGateway;

    public Mono<Client> findClientByIdentification(Client client) {
        return clientRepository.findClientByIdentification(client)
                .switchIfEmpty(Mono.error(new BusinessException(CLIENT_INACTIVE)));
    }

    public Mono<ResponseUpdateClient> inactivateClient(Client pClient) {
        return findClientByIdentification(pClient)
                .flatMap(clientRepository::inactivateClient)
                .map(client -> client.toBuilder()
                        .voucher(getVoucher()).build())
                .flatMap(client -> newnessUseCase.saveNewness(client, INACTIVE_CLIENT, client.getVoucher()))
                .flatMap(client -> getResponse(client.getVoucher(), SUCCESS_CHANGE));
    }

    public Mono<ResponseUpdateClient> saveClient(Enrol enrol, boolean isSeries, String voucher) {
        return clientRepository.findClientByIdentification(enrol.getClient())
                .flatMap(this::validateClientStatus)
                .flatMap(client -> updateClientMcd(enrol))
                .map(response -> response.getActual().getClient())
                .switchIfEmpty(createClientAndContacts(enrol, voucher))
                .filter(cc -> isSeries)
                .flatMap(pp -> getResponse(enrol.getClient().getVoucher(), SUCCESS_ENROLL))
                .switchIfEmpty(Mono.defer(() -> getResponseEnroll(enrol, voucher)));
    }

    private Mono<Client> createClientAndContacts(Enrol enrol, String voucher) {
        return contactUseCase.validateContacts(enrol)
                .flatMap(enrol2 -> validateDocument(enrol))
                .flatMap(this::validateCreationUserChannel)
                .flatMap(cli2 -> contactUseCase.validatePhone(enrol, cli2))
                .flatMap(cli3 -> contactUseCase.validateMail(enrol, cli3))
                .flatMap(clientRepository::saveClient)
                .flatMap(client -> newnessUseCase.saveNewness(client, CREATE_CLIENT, voucher))
                /*.flatMap(clientGateway::matchClientWithBasicKit)*/ // basic kit ms alert mgm
                .map(aBoolean -> enrol.getContactData())
                .flatMapMany(Flux::fromIterable)
                .map(contact -> contact.toBuilder().documentType(enrol.getClient().getDocumentType())
                        .documentNumber(enrol.getClient().getDocumentNumber())
                        .segment(enrol.getClient().getConsumerCode()).build())
                .flatMap(contact -> contactUseCase.saveContact(contact, voucher))
                .then(Mono.just(enrol.getClient()));
    }

    private Mono<ResponseUpdateClient> getResponse(String voucher, BusinessErrorMessage message) {
        return Mono.just(ResponseUpdateClient.builder()
                .idResponse(message.getCode())
                .description(message.getMessage())
                .voucherNumber(voucher)
                .build());
    }

    private Mono<Client> validateDocument(Enrol enrol) {
        return documentGateway.getDocument(enrol.getClient().getDocumentType())
                .switchIfEmpty(Mono.error(new BusinessException(DOCUMENT_TYPE_NOT_FOUND)))
                .map(Document::getId)
                .map(documentType -> enrol.getClient().toBuilder()
                        .idState(enrol.getClient().getStateClient()
                                .equals(ACTIVE.getValue()) ? ACTIVE.getType() : INACTIVE.getType())
                        .documentType(documentType)
                        .enrollmentOrigin(enrol.getClient().getConsumerCode()).build());
    }

    private Mono<ResponseUpdateClient> getResponseEnroll(Enrol enrol, String voucher) {
        return Mono.just(enrol)
                .flatMap(consumer -> getMapConsumerPersons(consumer, voucher)
                        .flatMap(commandGateway::sendCommandEnroll))
                .then(getResponse(voucher, SUCCESS_ENROLL));
    }

    private Mono<Bridge> getMapConsumerPersons(Enrol enrol, String voucher) {
        return consumerGateway.findConsumerById(enrol.getClient().getConsumerCode())
                .filter(cons -> cons.getSegment().equalsIgnoreCase(PersonType.PERSONAS))
                .map(enr -> getMapToSendToBridgeMQ(enrol, voucher));
    }

    private Mono<ResponseUpdateClient> getResponseUpdate(Enrol enrol, String voucher) {
        return Mono.just(enrol)
                .flatMap(consumer -> getMapConsumerPersons(consumer, voucher)
                        .flatMap(commandGateway::sendCommandUpdate))
                .then(getResponse(voucher, SUCCESS_UPDATE));
    }

    private Mono<StatusResponse<Enrol>> getResponseUpdateMcd(Enrol enrol, StatusResponse<Enrol> response) {
        return Mono.just(enrol)
                .flatMap(consumer -> getMapConsumerPersons(consumer, getVoucher())
                        .flatMap(commandGateway::sendCommandUpdate))
                .thenReturn(response);
    }

    private Mono<Client> validateClientStatus(Client pClient) {
        return Mono.just(pClient)
                .filter(client -> Objects.equals(client.getIdState(), INACTIVE.getType()))
                .switchIfEmpty(Mono.error(new BusinessException(CLIENT_ACTIVE)));
    }

    private Mono<Client> validateCreationUserChannel(Client pClient) {
        if (("SUC".equalsIgnoreCase(pClient.getConsumerCode()) || "GDE".equalsIgnoreCase(pClient.getConsumerCode()))
                && isNull(pClient.getCreationUser())) {
            return Mono.error(new BusinessException(USER_NOT_VALID_SSAL_TEL));
        }
        return Mono.just(pClient);
    }

    public Mono<StatusResponse<Enrol>> updateClientMcd(Enrol enrol) {
        return getResponseClient(enrol)
                .flatMap(response -> responseUpdate(enrol, response))
                .flatMap(enrolStatusResponse -> getResponseUpdateMcd(enrol, enrolStatusResponse));
    }


    public static boolean isNull(String str) {
        return Objects.isNull(str) || str.isEmpty();
    }

    public Mono<StatusResponse<Client>> getResponseClient(Enrol enrol) {
        return clientRepository.findClientByIdentification(enrol.getClient())
                .switchIfEmpty(Mono.error(new BusinessException(CLIENT_INACTIVE)))
                .flatMap(clientBefore -> buildResponse(clientBefore, enrol.getClient().toBuilder()
                        .idState(enrol.getClient().getStateClient()
                                .equalsIgnoreCase(ACTIVE.getValue()) ? ACTIVE.getType() : INACTIVE.getType())
                        .enrollmentOrigin(enrol.getClient().getConsumerCode()).build()))
                .flatMap(clientRepository::updateClient)
                .flatMap(response -> newnessUseCase.saveNewness(response.getBefore(), UPDATE_CLIENT, getVoucher())
                        .thenReturn(response));
    }

    private Mono<StatusResponse<Enrol>> responseUpdate(Enrol enrol, StatusResponse<Client> responseClient) {
        Enrol enrolActual = Enrol.builder().contactData(new ArrayList<>()).build();
        Enrol enrolBefore = Enrol.builder().contactData(new ArrayList<>()).build();
        StatusResponse<Enrol> responseUpdate = new StatusResponse<>(SUCCESS_ENROLL.getCode(), enrolActual, enrolBefore);
        return Flux.fromIterable(enrol.getContactData())
                .map(contact -> contact.toBuilder().documentType(enrol.getClient().getDocumentType())
                        .documentNumber(enrol.getClient().getDocumentNumber())
                        .segment(enrol.getClient().getConsumerCode())
                        .build())
                .flatMap(contact -> contactUseCase.updateContactRequest(contact, ""))
                .doOnNext(response -> responseUpdate.getActual().getContactData().add(response.getActual()))
                .doOnNext(response -> responseUpdate.getBefore().getContactData().add(response.getBefore()))
                .doOnNext(response -> responseUpdate.getActual().setClient(responseClient.getActual()))
                .doOnNext(response -> responseUpdate.getBefore().setClient(responseClient.getBefore()))
                .collectList()
                .map(items -> responseUpdate);
    }

    public Mono<ResponseUpdateClient> updateClient(Enrol enrol, Client client, boolean isISeries) {
        return Mono.just(enrol.getClient())
                .flatMap(cli2 -> contactUseCase.validatePhone(enrol, cli2))
                .flatMap(cli3 -> contactUseCase.validateMail(enrol, cli3))
                .flatMap(client1 -> getResponseClientISeriesBridge(client, enrol))
                .flatMap(resp -> Flux.fromIterable(enrol.getContactData())
                        .map(contact -> contact.toBuilder().documentType(enrol.getClient().getDocumentType())
                                .documentNumber(enrol.getClient().getDocumentNumber())
                                .segment(enrol.getClient().getConsumerCode())
                                .build())
                        .flatMap(contact -> contactUseCase.updateContactRequest(contact, client.getVoucher()))
                        .collectList()
                        .filter(cc -> isISeries)
                        .flatMap(pp -> getResponse(enrol.getClient().getVoucher(), SUCCESS_UPDATE))
                        .switchIfEmpty(Mono.defer(() -> getResponseUpdate(enrol, client.getVoucher()))));
    }

    public Mono<StatusResponse<Client>> getResponseClientISeriesBridge(Client client, Enrol enrol) {
        return Mono.just(client)
                .flatMap(clientBefore -> buildResponse(clientBefore, enrol.getClient().toBuilder()
                        .idState(enrol.getClient().getStateClient()
                                .equalsIgnoreCase(ACTIVE.getValue()) ? ACTIVE.getType() : INACTIVE.getType())
                        .enrollmentOrigin(enrol.getClient().getConsumerCode()).build()))
                .flatMap(clientRepository::updateClient)
                .flatMap(response -> newnessUseCase.saveNewness(response.getBefore(), UPDATE_CLIENT, client.getVoucher())
                        .thenReturn(response));
    }

    public Mono<ResponseUpdateClient> updateClientMono(Enrol enrol, boolean isISeries, String voucher) {
        return clientRepository.findClientByIdentification(enrol.getClient())
                .flatMap(client -> updateClient(enrol, client.toBuilder().voucher(voucher).build(), isISeries))
                .switchIfEmpty(Mono.defer(() -> createClientAndContacts(enrol, voucher)
                        .flatMap(client -> getResponseEnroll(enrol, voucher))));
    }

    private Mono<StatusResponse<Client>> buildResponse(Client before, Client actual) {
        return documentGateway.getDocument(actual.getDocumentType())
                .switchIfEmpty(Mono.error(new BusinessException(DOCUMENT_TYPE_NOT_FOUND)))
                .map(document -> StatusResponse.<Client>builder()
                        .before(before)
                        .actual(actual.toBuilder().documentType(document.getId()).build())
                        .build());
    }

    public Mono<Integer> deleteClient(Long documentInit, Long documentEnd) {
        return clientRepository.deleteClient(documentInit, documentEnd);
    }

}