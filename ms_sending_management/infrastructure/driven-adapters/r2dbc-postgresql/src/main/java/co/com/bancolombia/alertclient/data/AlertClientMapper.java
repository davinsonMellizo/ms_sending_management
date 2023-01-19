package co.com.bancolombia.alertclient.data;

import co.com.bancolombia.model.alertclient.AlertClient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AlertClientMapper {

    @Mapping(target = "numberOperations", defaultValue = "0")
    @Mapping(target = "amountEnable", defaultValue = "0L")
    @Mapping(target = "associationOrigin", defaultValue = "")
    @Mapping(target = "creationUser", defaultValue = "")
    AlertClient toEntity(AlertClientData alertClientData);

}
