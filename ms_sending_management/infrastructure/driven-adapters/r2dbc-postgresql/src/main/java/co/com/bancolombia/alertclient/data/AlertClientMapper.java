package co.com.bancolombia.alertclient.data;

import co.com.bancolombia.model.alertclient.AlertClient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AlertClientMapper {

    @Mapping(target = "numberOperations", defaultValue = "0")
    @Mapping(target = "amountEnable", defaultValue = "0L")
    AlertClient toEntity(AlertClientData alertClientData);

    @Mapping(target = "id", ignore = true)
    AlertClientData toData(AlertClient alertClient);
}
