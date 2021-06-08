package co.com.bancolombia.contactmedium.data;

import co.com.bancolombia.model.contactmedium.ContactMedium;
import co.com.bancolombia.model.state.State;
import co.com.bancolombia.state.data.StateData;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ContactMediumMapper {
    ContactMedium toEntity(ContactMediumData contactMediumData);
}
