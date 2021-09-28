package co.com.bancolombia.contact.data;

import co.com.bancolombia.model.contact.Contact;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ContactMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "idState", ignore = true)
    @Mapping(target = "idEnrollmentContact", ignore = true)
    @Mapping(target = "idContactMedium", ignore = true)
    Contact toEntity(ContactData contactData);

}
