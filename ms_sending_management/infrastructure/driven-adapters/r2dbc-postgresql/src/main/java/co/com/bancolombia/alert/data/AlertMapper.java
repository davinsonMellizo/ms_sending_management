package co.com.bancolombia.alert.data;

import co.com.bancolombia.model.alert.Alert;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AlertMapper {
    Alert toEntity(AlertData alertData);

    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "newAlert", ignore = true)
    AlertData toData(Alert alert);
}
