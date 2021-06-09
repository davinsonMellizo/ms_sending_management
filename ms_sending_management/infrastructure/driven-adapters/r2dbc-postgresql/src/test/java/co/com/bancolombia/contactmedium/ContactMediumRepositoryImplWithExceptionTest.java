package co.com.bancolombia.contactmedium;


import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.contactmedium.data.ContactMediumMapper;
import co.com.bancolombia.enrollmentcontact.data.EnrollmentContactMapper;
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
public class ContactMediumRepositoryImplWithExceptionTest {

    @InjectMocks
    private ContactMediumRepositoryImplement repositoryImplement;
    @Mock
    private ContactMediumRepository repository;
    @Spy
    private ContactMediumMapper mapper = Mappers.getMapper(ContactMediumMapper .class);

    @Test
    public void findContactMediumByCodeWithException() {
        when(repository.findById(anyString()))
                .thenReturn(Mono.error(RuntimeException::new));
        repositoryImplement.findContactMediumByCode("SMS")
                .as(StepVerifier::create)
                .expectError(TechnicalException.class)
                .verify();
    }
}