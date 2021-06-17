package co.com.bancolombia.contactmedium.data;

import co.com.bancolombia.model.contactmedium.ContactMedium;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ContactMediumMapper {
    ContactMedium toEntity(ContactMediumData contactMediumData);
}
