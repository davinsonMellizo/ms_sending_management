package co.com.bancolombia.client.data;

import co.com.bancolombia.model.client.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    @Mapping(target = "creationUser", defaultValue = "")
    Client toEntity(ClientData clientData);

    ClientData toData(Client client);
}
