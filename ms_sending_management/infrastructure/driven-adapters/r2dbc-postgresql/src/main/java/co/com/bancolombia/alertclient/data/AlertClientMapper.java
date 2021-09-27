package co.com.bancolombia.alertclient.data;

import co.com.bancolombia.config.model.alertclient.AlertClient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AlertClientMapper {

    AlertClient toEntity(AlertClientData alertClientData);

    @Mapping(target = "id", ignore = true)
    AlertClientData toData(AlertClient alertClient);
}
