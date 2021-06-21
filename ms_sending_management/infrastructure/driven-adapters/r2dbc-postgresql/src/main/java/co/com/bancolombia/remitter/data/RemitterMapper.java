package co.com.bancolombia.remitter.data;

import co.com.bancolombia.model.remitter.Remitter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RemitterMapper {
    Remitter toEntity(RemitterData RemitterData);

    @Mapping(target = "isNew", ignore = true)
    RemitterData toData(Remitter Remitter);
}
