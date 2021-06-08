package co.com.bancolombia.enrollmentcontact;


import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.enrollmentcontact.data.EnrollmentContactData;
import co.com.bancolombia.enrollmentcontact.data.EnrollmentContactMapper;
import co.com.bancolombia.model.enrollmentcontact.EnrollmentContact;
import co.com.bancolombia.state.data.StateMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class EnrollmentContactRepositoryImplWithExceptionTest {

    @InjectMocks
    private EnrollmentContactRepositoryImplement repositoryImplement;
    @Mock
    private EnrollmentContactRepository repository;
    @Spy
    private EnrollmentContactMapper mapper = Mappers.getMapper(EnrollmentContactMapper.class);

    @Test
    public void findEnrollmentContactByCodeWithException() {
        when(repository.findById(anyString()))
                .thenReturn(Mono.error(RuntimeException::new));
        repositoryImplement.findEnrollmentContactByCode("ALM")
                .as(StepVerifier::create)
                .expectError(TechnicalException.class)
                .verify();
    }
}