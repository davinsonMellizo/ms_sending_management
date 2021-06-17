package co.com.bancolombia.enrollmentcontact;

import co.com.bancolombia.AdapterOperations;
import co.com.bancolombia.commons.enums.TechnicalExceptionEnum;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.enrollmentcontact.data.EnrollmentContactData;
import co.com.bancolombia.enrollmentcontact.data.EnrollmentContactMapper;
import co.com.bancolombia.model.enrollmentcontact.EnrollmentContact;
import co.com.bancolombia.model.enrollmentcontact.gateways.EnrollmentContactGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class EnrollmentContactRepositoryImplement
        extends AdapterOperations<EnrollmentContact, EnrollmentContactData, String, EnrollmentContactRepository>
        implements EnrollmentContactGateway {


    @Autowired
    public EnrollmentContactRepositoryImplement(EnrollmentContactRepository repository, EnrollmentContactMapper mapper) {
        super(repository, null, mapper::toEntity);
    }


    @Override
    public Mono<EnrollmentContact> findEnrollmentContactByCode(String code) {
        return doQuery(repository.findById(code))
                .onErrorMap(e -> new TechnicalException(e, TechnicalExceptionEnum.FIND_ENROLLMENT_CONTACT_ERROR));
    }
}
