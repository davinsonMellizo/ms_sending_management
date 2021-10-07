package co.com.bancolombia.contact.data;

import co.com.bancolombia.model.contact.Contact;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ContactMapper {
    //@Mapping(source ="idContactMedium" ,target = "contactMedium")
    //@Mapping(source ="idState" ,target = "state")
    Contact toEntity(ContactData contactData);

    @Mapping(source ="contactMedium" ,target = "idContactMedium")
    @Mapping(source ="state" ,target = "idState")
    ContactData toData(Contact contact);
}
