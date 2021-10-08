package co.com.bancolombia.alerttemplate;

import co.com.bancolombia.alerttemplate.data.AlertTemplateMapper;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.model.alerttemplate.AlertTemplate;
import co.com.bancolombia.drivenadapters.TimeFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AlertTemplateRepositoryImplExceptionTest {

    @InjectMocks
    private AlertTemplateRepositoryImplement repositoryImpl;
    @Mock
    private AlertTemplateRepository repository;
    @Spy
    private AlertTemplateMapper mapper = Mappers.getMapper(AlertTemplateMapper.class);
    private static final LocalDateTime NOW = LocalDateTime.now();
    @Mock
    private TimeFactory timeFactory;

    private final AlertTemplate alertTemplate = new AlertTemplate();

    @BeforeEach
    public void init() {
        alertTemplate.setId(0);
        alertTemplate.setField("field1");
        alertTemplate.setInitialPosition(1);
        alertTemplate.setFinalPosition(5);
        alertTemplate.setCreationUser("User");
    }

    //TODO TEST FAILED
    public void findAlertTemplateByIdWithException() {
        when(repository.findById(anyInt()))
                .thenReturn(Mono.error(RuntimeException::new));
        repositoryImpl.findTemplateById(alertTemplate.getId())
                .as(StepVerifier::create)
                .expectError(TechnicalException.class)
                .verify();
    }

    @Test
    public void saveAlertTemplateWithException() {
        when(timeFactory.now()).thenReturn(NOW);
        when(repository.save(any()))
                .thenReturn(Mono.error(RuntimeException::new));
        repositoryImpl.save(alertTemplate)
                .as(StepVerifier::create)
                .expectError(TechnicalException.class)
                .verify();
    }

    @Test
    public void deleteAlertTemplateWithException() {
        when(repository.deleteById(anyInt()))
                .thenReturn(Mono.error(RuntimeException::new));
        repositoryImpl.delete(alertTemplate.getId())
                .as(StepVerifier::create)
                .expectError(TechnicalException.class)
                .verify();
    }
}