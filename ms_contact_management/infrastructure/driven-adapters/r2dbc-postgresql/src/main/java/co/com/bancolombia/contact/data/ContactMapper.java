package co.com.bancolombia.contact.data;

import co.com.bancolombia.model.contact.Contact;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ContactMapper {
    Contact toEntity(ContactData contactData);

    @Mapping(source ="contactMedium" ,target = "idContactMedium")
    @Mapping(source ="state" ,target = "idState")
    @Mapping(target = "id", ignore = true)
    ContactData toData(Contact contact);
}
