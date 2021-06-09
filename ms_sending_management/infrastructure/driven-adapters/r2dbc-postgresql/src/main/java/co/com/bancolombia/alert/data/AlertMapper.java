package co.com.bancolombia.alert.data;

import co.com.bancolombia.model.alert.Alert;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AlertMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "idState", ignore = true)
    @Mapping(target = "idEnrollmentContact", ignore = true)
    @Mapping(target = "idContactMedium", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    Alert toEntity(AlertData alertData);

    AlertData toData(Alert alert);
}
