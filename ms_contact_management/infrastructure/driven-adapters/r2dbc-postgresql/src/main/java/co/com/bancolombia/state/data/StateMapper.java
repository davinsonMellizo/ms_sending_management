package co.com.bancolombia.state.data;

import co.com.bancolombia.contact.data.ContactData;
import co.com.bancolombia.model.contact.Contact;
import co.com.bancolombia.model.state.State;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StateMapper {
    State toEntity(StateData stateData);
}
