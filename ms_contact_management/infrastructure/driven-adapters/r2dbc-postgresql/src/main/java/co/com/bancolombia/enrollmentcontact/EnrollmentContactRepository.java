package co.com.bancolombia.enrollmentcontact;

import co.com.bancolombia.enrollmentcontact.data.EnrollmentContactData;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface EnrollmentContactRepository extends ReactiveCrudRepository<EnrollmentContactData, String> {

}
