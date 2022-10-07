package co.com.bancolombia.provider.data;

import co.com.bancolombia.model.provider.Provider;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProviderMapper {
    Provider toEntity(ProviderData providerData);


}
