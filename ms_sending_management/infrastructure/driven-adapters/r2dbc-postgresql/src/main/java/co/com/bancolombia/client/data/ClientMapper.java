package co.com.bancolombia.client.data;

import co.com.bancolombia.model.client.Client;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    Client toEntity(ClientData clientData);

}
