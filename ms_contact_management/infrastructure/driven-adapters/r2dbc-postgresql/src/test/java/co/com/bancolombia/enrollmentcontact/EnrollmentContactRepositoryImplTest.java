package co.com.bancolombia.enrollmentcontact;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class EnrollmentContactRepositoryImplTest {

    @Autowired
    private EnrollmentContactRepositoryImplement repositoryImplement;

    @Test
    public void findEnrollmentContactByCode() {
        StepVerifier.create(repositoryImplement.findEnrollmentContactByCode("ALM"))
                .expectNextCount(1)
                .verifyComplete();
    }
}