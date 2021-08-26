package co.com.bancolombia.client;

import co.com.bancolombia.AdapterOperations;
import co.com.bancolombia.client.data.ClientData;
import co.com.bancolombia.client.data.ClientMapper;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.model.client.Client;
import co.com.bancolombia.model.client.gateways.ClientGateway;
import co.com.bancolombia.model.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.FIND_CLIENT_ERROR;

@Repository
public class ClientRepositoryImplement
        extends AdapterOperations<Client, ClientData, Long, ClientRepository>
        implements ClientGateway {

    @Autowired
    public ClientRepositoryImplement(ClientRepository repository, ClientMapper mapper) {
        super(repository, null, mapper::toEntity);
    }

    @Override
    public Mono<Client> findClientByIdentification(Message message) {
        return repository.findClientByIdentification(message.getDocumentNumber(), message.getDocumentType())
                .map(this::convertToEntity)
                .onErrorMap(e -> new TechnicalException(e, FIND_CLIENT_ERROR));
    }

}
