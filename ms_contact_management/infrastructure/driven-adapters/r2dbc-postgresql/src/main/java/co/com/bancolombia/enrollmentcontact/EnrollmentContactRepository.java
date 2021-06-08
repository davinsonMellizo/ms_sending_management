package co.com.bancolombia.enrollmentcontact;

import co.com.bancolombia.enrollmentcontact.data.EnrollmentContactData;
import co.com.bancolombia.model.enrollmentcontact.EnrollmentContact;
import co.com.bancolombia.state.data.StateData;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface EnrollmentContactRepository extends ReactiveCrudRepository<EnrollmentContactData, String> {

}
