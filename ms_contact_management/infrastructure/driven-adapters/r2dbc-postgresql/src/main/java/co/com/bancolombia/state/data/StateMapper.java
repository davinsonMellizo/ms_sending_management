package co.com.bancolombia.state.data;

import co.com.bancolombia.model.state.State;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StateMapper {
    State toEntity(StateData stateData);
}
