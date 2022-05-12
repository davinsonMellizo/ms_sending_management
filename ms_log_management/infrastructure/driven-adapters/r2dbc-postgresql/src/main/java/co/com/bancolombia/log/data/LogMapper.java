package co.com.bancolombia.log.data;

import co.com.bancolombia.model.log.Log;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LogMapper {
    LogData toData(Log provider);
}
