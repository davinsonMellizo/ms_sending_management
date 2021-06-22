package co.com.bancolombia.service.data;

import co.com.bancolombia.model.service.Service;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ServiceMapper {
    Service toEntity(ServiceData serviceData);

    @Mapping(target = "isNew", ignore = true)
    ServiceData toData(Service service);
}
