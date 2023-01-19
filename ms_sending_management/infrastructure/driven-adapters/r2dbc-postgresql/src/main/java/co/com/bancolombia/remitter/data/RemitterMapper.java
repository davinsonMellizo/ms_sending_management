package co.com.bancolombia.remitter.data;

import co.com.bancolombia.model.remitter.Remitter;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RemitterMapper {
    Remitter toEntity(RemitterData remitterData);

}
