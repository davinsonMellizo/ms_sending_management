package co.com.bancolombia.alert.data;

import co.com.bancolombia.model.alert.Alert;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AlertMapper {
    Alert toEntity(AlertData alertData);

    @Mapping(target = "isNew", ignore = true)
    AlertData toData(Alert alert);
}
