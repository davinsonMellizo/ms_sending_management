package co.com.bancolombia.prefix.data;

import co.com.bancolombia.model.prefix.Prefix;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PrefixMapper {
    Prefix toEntity(PrefixData prefixData);
}
