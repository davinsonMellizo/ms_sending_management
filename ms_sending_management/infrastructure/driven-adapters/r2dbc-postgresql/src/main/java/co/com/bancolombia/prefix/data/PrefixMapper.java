package co.com.bancolombia.prefix.data;

import co.com.bancolombia.model.prefix.Prefix;
import co.com.bancolombia.model.service.Service;
import co.com.bancolombia.service.data.ServiceData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PrefixMapper {
    Prefix toEntity(PrefixData prefixData);
}
