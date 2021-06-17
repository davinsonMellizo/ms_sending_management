package co.com.bancolombia.enrollmentcontact.data;

import co.com.bancolombia.model.enrollmentcontact.EnrollmentContact;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EnrollmentContactMapper {
    EnrollmentContact toEntity(EnrollmentContactData enrollmentContactData);
}
