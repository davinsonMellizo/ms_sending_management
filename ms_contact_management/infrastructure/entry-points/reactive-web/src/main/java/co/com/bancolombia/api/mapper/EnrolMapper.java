package co.com.bancolombia.api.mapper;

import co.com.bancolombia.api.dto.ContactDTO;
import co.com.bancolombia.api.dto.EnrolDTO;
import co.com.bancolombia.model.client.Enrol;
import co.com.bancolombia.model.contact.Contact;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EnrolMapper {

    @Mapping(target = "client.id", ignore = true)
    @Mapping(target = "client.createdDate", ignore = true)
    @Mapping(target = "client.modifiedDate", ignore = true)
    Enrol toEntity(EnrolDTO clientData);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "consumer",target = "segment")
    @Mapping(target = "modifiedDate", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "previous", ignore = true)
    Contact toEntity(ContactDTO contactDTO);
}
