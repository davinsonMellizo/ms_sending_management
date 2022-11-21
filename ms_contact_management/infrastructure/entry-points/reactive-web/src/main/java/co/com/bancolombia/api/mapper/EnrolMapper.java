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
    @Mapping(target = "client.enrollmentOrigin", ignore = true)
    @Mapping(target = "client.idState", ignore = true)
    @Mapping(target = "client.voucher", ignore = true)
    @Mapping(source = "client.identification.documentType", target = "client.documentType")
    @Mapping(source = "client.identification.documentNumber", target = "client.documentNumber")
    @Mapping(source = "client.traceability.consumerCode", target = "client.consumerCode")
    @Mapping(source = "client.traceability.creationUser", target = "client.creationUser")
    Enrol toEntity(EnrolDTO clientData);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "segment", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "previous", ignore = true)
    @Mapping(target = "documentNumber", ignore = true)
    @Mapping(target = "documentType", ignore = true)
    @Mapping(target = "idState", ignore = true)
    Contact toEntityContact(ContactDTO contactDTO);
}
