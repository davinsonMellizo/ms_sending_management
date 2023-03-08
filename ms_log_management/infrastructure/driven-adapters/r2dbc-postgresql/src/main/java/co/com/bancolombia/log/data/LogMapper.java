package co.com.bancolombia.log.data;

import co.com.bancolombia.model.log.Log;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LogMapper {
    LogData toData(Log provider);
}
