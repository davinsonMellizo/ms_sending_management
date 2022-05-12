package co.com.bancolombia.priority.data;

import co.com.bancolombia.model.priority.Priority;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PriorityMapper {

    Priority toEntity(PriorityData priorityData);

    @Mapping(target = "isNew", ignore = true)
    PriorityData toData(Priority priority);
}
