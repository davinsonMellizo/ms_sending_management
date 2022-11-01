package co.com.bancolombia.usecase.client;

import co.com.bancolombia.commons.constants.PersonType;
import co.com.bancolombia.commons.enums.BusinessErrorMessage;
import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.bridge.Bridge;
import co.com.bancolombia.model.client.Client;
import co.com.bancolombia.model.client.Enrol;
import co.com.bancolombia.model.client.ResponseUpdateClient;
import co.com.bancolombia.model.client.gateways.ClientRepository;
import co.com.bancolombia.model.consumer.gateways.ConsumerGateway;
import co.com.bancolombia.model.document.Document;
import co.com.bancolombia.model.document.gateways.DocumentGateway;
import co.com.bancolombia.model.events.gateways.CommandGateway;
import co.com.bancolombia.model.response.StatusResponse;
import co.com.bancolombia.model.state.gateways.StateGateway;
import co.com.bancolombia.usecase.contact.ContactUseCase;
import co.com.bancolombia.usecase.log.NewnessUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Objects;

import static co.com.bancolombia.commons.constants.Transaction.CREATE_CLIENT;
import static co.com.bancolombia.commons.constants.Transaction.INACTIVE_CLIENT;
import static co.com.bancolombia.commons.constants.Transaction.UPDATE_CLIENT;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.CLIENT_NOT_FOUND;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.CLIENT_REGISTERED;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.DOCUMENT_TYPE_NOT_FOUND;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.STATE_NOT_FOUND;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.SUCCESS_CHANGE;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.SUCCESS_ENROLL;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.SUCCESS_UPDATE;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.USER_NOT_VALID_SSAL_TEL;
import static co.com.bancolombia.commons.enums.State.ACTIVE;
import static co.com.bancolombia.commons.enums.State.INACTIVE;
import static co.com.bancolombia.usecase.commons.BridgeContact.*;

@RequiredArgsConstructor
public class ClientUseCase {

    private final ClientRepository clientRepository;
    private final DocumentGateway documentGateway;
    private final StateGateway stateGateway;
    private final ContactUseCase contactUseCase;
    private final NewnessUseCase newnessUseCase;
    private final CommandGateway commandGateway;
    private final ConsumerGateway consumerGateway;

    public Mono<Client> findClientByIdentification(Client client) {
        return clientRepository.findClientByIdentification(client)
                .switchIfEmpty(Mono.error(new BusinessException(CLIENT_NOT_FOUND)));
    }

    public Mono<ResponseUpdateClient> inactivateClient(Client pClient) {
        return findClientByIdentification(pClient)
                .flatMap(clientRepository::inactivateClient)
                .map(client -> client.toBuilder()
                        .voucher(getVoucher()).build())
                .flatMap(client -> newnessUseCase.saveNewness(client, INACTIVE_CLIENT, client.getVoucher()))
                .flatMap(client -> getResponse(client.getVoucher(), SUCCESS_CHANGE));
    }

    public Mono<ResponseUpdateClient> saveClientRequest(Enrol enrol, boolean isSeries, String voucher) {
        return clientRepository.findClientByIdentification(enrol.getClient())
                .flatMap(this::validateClientStatus)
                .flatMap(client -> updateClientAndContacts(enrol, client.toBuilder().voucher(voucher).build(), isSeries))
                .flatMap(statusResponse -> getResponse(enrol.getClient().getVoucher(), SUCCESS_UPDATE))
                .switchIfEmpty(createClientAndContacts(enrol, voucher, isSeries)
                        .then(getResponse(voucher, SUCCESS_ENROLL)));

    }

    private Mono<Client> validateClientStatus(Client pClient) {
        return Mono.just(pClient)
                .filter(client -> Objects.equals(client.getIdState(), INACTIVE.getType()))
                .switchIfEmpty(Mono.error(new BusinessException(CLIENT_REGISTERED)));
    }

    private Mono<StatusResponse<Enrol>> createClientAndContacts(Enrol enrol, String voucher, boolean isIseries) {
        Enrol enrolActual = Enrol.builder().contactData(new ArrayList<>()).build();
        Enrol enrolBefore = Enrol.builder().contactData(new ArrayList<>()).build();
        StatusResponse<Enrol> responseCreate = new StatusResponse<>(SUCCESS_ENROLL.getCode(), enrolActual, enrolBefore);
        return validateDataClient(enrol)
                .flatMap(contactUseCase::validatePhone)
                .flatMap(contactUseCase::validateMail)
                .flatMap(contactUseCase::validateContacts)
                .doOnNext(enrolValidated -> enrol.setContactData(enrolValidated.getContactData()))
                .flatMap(this::validateCreationUserChannel)
                .map(Enrol::getClient)
                .flatMap(clientRepository::saveClient)
                .doOnNext(enrol::setClient)
                .doOnNext(client -> responseCreate.getActual().setClient(client))
                .flatMap(client -> newnessUseCase.saveNewness(client, CREATE_CLIENT, voucher))
                .map(aBoolean -> enrol.getContactData())
                .flatMapMany(Flux::fromIterable)
                .flatMap(contact -> contactUseCase.saveContact(contact, voucher))
                .doOnNext(response -> responseCreate.getActual().getContactData().add(response))
                .last()
                .then(sendCreateToIseries(enrol,voucher,responseCreate,isIseries));
    }

    private Mono<Enrol> validateDataClient(Enrol enrol) {
        return documentGateway.getDocument(enrol.getClient().getDocumentType())
                .switchIfEmpty(Mono.error(new BusinessException(DOCUMENT_TYPE_NOT_FOUND)))
                .map(Document::getId)
                .map(documentType -> enrol.getClient().toBuilder()
                        .documentType(documentType)
                        .enrollmentOrigin(enrol.getClient().getConsumerCode()).build())
                .doOnNext(client -> enrol.setClient(client))
                .flatMap(client -> stateGateway.findState(enrol.getClient().getStateClient()))
                .switchIfEmpty(Mono.error(new BusinessException(STATE_NOT_FOUND)))
                .map(state -> enrol.getClient().toBuilder().idState(state.getId()).build())
                .map(client -> enrol.toBuilder().client(client).build());
    }

    private Mono<Enrol> validateCreationUserChannel(Enrol enrol) {
        return Mono.just(enrol.getClient().getConsumerCode())
                .filter(consumer -> ("SUC".equalsIgnoreCase(consumer) || "GDE".equalsIgnoreCase(consumer)))
                .map(consumer -> enrol.getClient().getCreationUser())
                .filter(user -> Objects.isNull(user) || user.isEmpty())
                .flatMap(enrol1 -> Mono.error(new BusinessException(USER_NOT_VALID_SSAL_TEL)))
                .then(Mono.just(enrol));
    }

    public Mono<StatusResponse<Enrol>> updateClientMcd(Enrol enrol) {
        return clientRepository.findClientByIdentification(enrol.getClient())
                .flatMap(client -> updateClientAndContacts(enrol, client.toBuilder().voucher("0").build(), false))
                .switchIfEmpty(Mono.defer(() -> createClientAndContacts(enrol, "0", false)));
    }

    public Mono<ResponseUpdateClient> updateClientRequest(Enrol enrol, boolean isISeries, String voucher) {
        return clientRepository.findClientByIdentification(enrol.getClient())
                .flatMap(client -> updateClientAndContacts(enrol, client.toBuilder().voucher(voucher).build(), isISeries))
                .flatMap(enrolStatusResponse -> getResponse(voucher, SUCCESS_UPDATE))
                .switchIfEmpty(Mono.defer(() -> createClientAndContacts(enrol, voucher, isISeries)
                        .flatMap(statusResponse -> getResponse(voucher, SUCCESS_ENROLL))));
    }

    public Mono<StatusResponse<Enrol>> updateClientAndContacts(Enrol enrol, Client client, boolean isISeries) {
        Enrol enrolActual = Enrol.builder().contactData(new ArrayList<>()).build();
        Enrol enrolBefore = Enrol.builder().contactData(new ArrayList<>()).build();
        StatusResponse<Enrol> responseUpdate = new StatusResponse<>(SUCCESS_ENROLL.getCode(), enrolActual, enrolBefore);
        return Mono.just(enrol)
                .flatMap(this::validateCreationUserChannel)
                .flatMap(contactUseCase::validatePhone)
                .flatMap(contactUseCase::validateMail)
                .flatMap(client1 -> updateClient(client, enrol))
                .flatMap(statusResponse -> Flux.fromIterable(enrol.getContactData())
                        .map(contact -> contact.toBuilder().documentType(enrol.getClient().getDocumentType())
                                .documentNumber(enrol.getClient().getDocumentNumber())
                                .segment(enrol.getClient().getConsumerCode())
                                .build())
                        .flatMap(contact -> contactUseCase.updateContactRequest(contact, client.getVoucher()))
                        .doOnNext(response -> responseUpdate.getActual().getContactData().add(response.getActual()))
                        .doOnNext(response -> responseUpdate.getBefore().getContactData().add(response.getBefore()))
                        .doOnNext(response -> responseUpdate.getActual().setClient(statusResponse.getActual()))
                        .doOnNext(response -> responseUpdate.getBefore().setClient(statusResponse.getBefore()))
                        .last()
                        .then(sendUpdateToIseries(enrol, client.getVoucher(), responseUpdate,isISeries)));
    }

    public Mono<StatusResponse<Client>> updateClient(Client client, Enrol enrol) {
        return Mono.just(client)
                .flatMap(clientBefore -> buildRequestToUpdateClient(clientBefore, enrol.getClient().toBuilder()
                        .idState(enrol.getClient().getStateClient()
                                .equalsIgnoreCase(ACTIVE.getValue()) ? ACTIVE.getType() : INACTIVE.getType())
                        .enrollmentOrigin(enrol.getClient().getConsumerCode()).build()))
                .flatMap(clientRepository::updateClient)
                .flatMap(response -> newnessUseCase.saveNewness(response.getBefore(), UPDATE_CLIENT, client.getVoucher())
                        .thenReturn(response));
    }

    private <T> Mono<T> sendUpdateToIseries(Enrol enrol, String voucher, T response, boolean isIseries) {
        return Mono.just(isIseries)
                .filter(aBoolean -> !isIseries)
                .flatMap(aBoolean -> getMapConsumerPersons(enrol, voucher))
                .flatMap(commandGateway::sendCommandUpdate)
                .then(Mono.just(response));
    }

    private <T> Mono<T> sendCreateToIseries(Enrol enrol, String voucher, T response, boolean isIseries) {
        return Mono.just(isIseries)
                .filter(aBoolean -> !isIseries)
                .flatMap(aBoolean -> getMapConsumerPersons(enrol, voucher))
                .flatMap(commandGateway::sendCommandEnroll)
                .then(Mono.just(response));
    }

    private Mono<ResponseUpdateClient> getResponse(String voucher, BusinessErrorMessage message) {
        return Mono.just(ResponseUpdateClient.builder()
                .idResponse(message.getCode())
                .description(message.getMessage())
                .voucherNumber(voucher)
                .build());
    }

    private Mono<Bridge> getMapConsumerPersons(Enrol enrol, String voucher) {
        return consumerGateway.findConsumerById(enrol.getClient().getConsumerCode())
                .filter(consumer -> consumer.getSegment().equalsIgnoreCase(PersonType.PERSONAS))
                .flatMap(consumer -> documentGateway.getDocument(enrol.getClient().getDocumentType()))
                .map(document -> getMapToSendToBridgeMQ(enrol, voucher, document));
    }


    private Mono<StatusResponse<Client>> buildRequestToUpdateClient(Client before, Client actual) {
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