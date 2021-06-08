package co.com.bancolombia.enrollmentcontact.data;

import co.com.bancolombia.model.enrollmentcontact.EnrollmentContact;
import co.com.bancolombia.model.state.State;
import co.com.bancolombia.state.data.StateData;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EnrollmentContactMapper {
    EnrollmentContact toEntity(EnrollmentContactData enrollmentContactData);
}
