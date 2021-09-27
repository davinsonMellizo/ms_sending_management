package co.com.bancolombia.provider.data;

import co.com.bancolombia.config.model.provider.Provider;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProviderMapper {
    Provider toEntity(ProviderData providerData);

    @Mapping(target = "isNew", ignore = true)
    ProviderData toData(Provider provider);
}
