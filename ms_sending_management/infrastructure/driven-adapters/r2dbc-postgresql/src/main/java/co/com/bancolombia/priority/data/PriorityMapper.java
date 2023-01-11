package co.com.bancolombia.priority.data;

import co.com.bancolombia.model.priority.Priority;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PriorityMapper {

    Priority toEntity(PriorityData priorityData);

}
