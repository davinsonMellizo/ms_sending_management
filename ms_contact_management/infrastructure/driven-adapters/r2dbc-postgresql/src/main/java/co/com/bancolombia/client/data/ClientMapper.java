package co.com.bancolombia.client.data;

import co.com.bancolombia.model.client.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    Client toEntity(ClientData clientData);

    @Mapping(target = "id", ignore = true)
    ClientData toData(Client client);
}
