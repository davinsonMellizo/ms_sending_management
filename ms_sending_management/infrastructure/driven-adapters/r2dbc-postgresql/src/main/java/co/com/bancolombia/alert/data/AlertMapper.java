package co.com.bancolombia.alert.data;

import co.com.bancolombia.model.alert.Alert;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AlertMapper {
    Alert toEntity(AlertData alertData);

}
