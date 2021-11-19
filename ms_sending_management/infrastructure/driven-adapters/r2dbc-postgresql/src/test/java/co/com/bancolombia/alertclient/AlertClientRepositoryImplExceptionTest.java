package co.com.bancolombia.alertclient;

import co.com.bancolombia.alertclient.data.AlertClientMapper;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.drivenadapters.TimeFactory;
import co.com.bancolombia.model.alertclient.AlertClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AlertClientRepositoryImplExceptionTest {

    @InjectMocks
    private AlertClientRepositoryImplement repositoryImpl;
    @Mock
    private AlertClientRepository repository;
    @Spy
    private AlertClientMapper mapper = Mappers.getMapper(AlertClientMapper.class);
    private static final LocalDateTime NOW = LocalDateTime.now();
    @Mock
    private TimeFactory timeFactory;

    private final AlertClient alertClient = new AlertClient();

    @BeforeEach
    public void init() {
        alertClient.setIdAlert("HGD");
        alertClient.setDocumentNumber(1L);
        alertClient.setDocumentType(0);
        alertClient.setNumberOperations(5);
        alertClient.setAmountEnable(9L);
        alertClient.setAccumulatedOperations(8);
        alertClient.setAccumulatedAmount(1L);
        alertClient.setAssociationOrigin("ac");
        alertClient.setCreationUser("user");
    }

    @Test
    public void saveAlertTemplateWithException() {
        when(timeFactory.now()).thenReturn(NOW);
        when(repository.save(any()))
                .thenReturn(Mono.error(RuntimeException::new));
        repositoryImpl.save(alertClient)
                .as(StepVerifier::create)
                .expectError(TechnicalException.class)
                .verify();
    }

    public void findAllAlertClient() {
        when(repository.alertsClientVisibleChannelByClient(anyLong(), anyInt()))
                .thenReturn(Flux.error(RuntimeException::new));
        repositoryImpl.alertsVisibleChannelByClient(1L, 0)
                .as(StepVerifier::create)
                .expectError(TechnicalException.class)
                .verify();
    }

    public void updateAlertClient() {
        when(timeFactory.now()).thenReturn(NOW);
        when(repository.updateAlertClient(anyInt(), anyLong(), anyString(), anyLong(), anyInt()))
                .thenReturn(Mono.error(RuntimeException::new));
        repositoryImpl.updateAlertClient(alertClient)
                .as(StepVerifier::create)
                .expectError(TechnicalException.class)
                .verify();
    }
}