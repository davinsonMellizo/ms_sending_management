package co.com.bancolombia.alert;

import co.com.bancolombia.alert.data.AlertData;
import co.com.bancolombia.alert.data.AlertMapper;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.drivenadapters.TimeFactory;
import co.com.bancolombia.model.alert.Alert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class AlertRepositoryImplExceptionTest {

    @InjectMocks
    private AlertRepositoryImplement repositoryImpl;
    @Mock
    private AlertRepository repository;
    @Spy
    private AlertMapper mapper = Mappers.getMapper(AlertMapper.class);
    private static final LocalDateTime NOW = LocalDateTime.now();
    @Mock
    private TimeFactory timeFactory;

    private final Alert alert = new Alert();
    private final AlertData alertData = new AlertData();

    @BeforeEach
    public void init() {
        alert.setId("HGD");
        alertData.setId("HGD");
    }

    @Test
    void findAlertByIdWithException() {
        when(repository.findAlertById(anyString()))
                .thenReturn(Mono.error(RuntimeException::new));
        repositoryImpl.findAlertById(alert.getId())
                .as(StepVerifier::create)
                .expectError(TechnicalException.class)
                .verify();
    }
}