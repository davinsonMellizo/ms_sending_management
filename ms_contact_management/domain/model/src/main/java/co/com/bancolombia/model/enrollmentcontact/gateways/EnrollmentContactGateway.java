package co.com.bancolombia.model.enrollmentcontact.gateways;

import co.com.bancolombia.model.enrollmentcontact.EnrollmentContact;
import co.com.bancolombia.model.state.State;
import reactor.core.publisher.Mono;

public interface EnrollmentContactGateway {
    Mono<EnrollmentContact> findEnrollmentContactByCode(String code);
}