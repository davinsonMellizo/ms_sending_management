package co.com.bancolombia.providerservice.data;

import co.com.bancolombia.model.providerservice.ProviderService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProviderServiceMapper {

    ProviderService toEntity(ProviderServiceData providerServiceData);

    @Mapping(target = "code", ignore = true)
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "service", ignore = true)
    @Mapping(target = "isNew", ignore = true)
    ProviderServiceData toData(ProviderService providerService);

}
