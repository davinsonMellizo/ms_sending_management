package co.com.bancolombia.alerttransaction;

import co.com.bancolombia.alerttransaction.data.AlertTransactionMapper;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.drivenadapters.TimeFactory;
import co.com.bancolombia.model.alerttransaction.AlertTransaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class AlertTransactionRepositoryImplExceptionTest {

    @InjectMocks
    private AlertTransactionRepositoryImplement repositoryImpl;
    @Mock
    private AlertTransactionRepository repository;
    @Spy
    private AlertTransactionMapper mapper = Mappers.getMapper(AlertTransactionMapper.class);
    private static final LocalDateTime NOW = LocalDateTime.now();
    @Mock
    private TimeFactory timeFactory;

    private final AlertTransaction alert = new AlertTransaction();

    @BeforeEach
    public void init() {
        alert.setIdAlert("HGD");
        alert.setIdTransaction("0358");
        alert.setIdConsumer("BLM");
    }


    @Test
    void findAllAlertTransactionWithException() {
        when(repository.findAllAlertTransaction(anyString()))
                .thenReturn(Flux.error(RuntimeException::new));
        repositoryImpl.findAllAlertTransaction(alert.getIdAlert())
                .as(StepVerifier::create)
                .expectError(TechnicalException.class)
                .verify();
    }

    @Test
    void saveAlertTransactionWithException() {
        when(timeFactory.now()).thenReturn(NOW);
        when(repository.save(any()))
                .thenReturn(Mono.error(RuntimeException::new));
        repositoryImpl.saveAlertTransaction(alert)
                .as(StepVerifier::create)
                .expectError(TechnicalException.class)
                .verify();
    }

    @Test
    void deleteRemitterWithException() {
        when(repository.deleteAlertTransaction(anyString(), anyString(), anyString()))
                .thenReturn(Mono.error(RuntimeException::new));
        repositoryImpl.deleteAlertTransaction(alert)
                .as(StepVerifier::create)
                .expectError(TechnicalException.class)
                .verify();
    }
}