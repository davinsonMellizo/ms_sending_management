package co.com.bancolombia.service;

import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.model.service.Service;
import co.com.bancolombia.drivenadapters.TimeFactory;
import co.com.bancolombia.service.data.ServiceData;
import co.com.bancolombia.service.data.ServiceMapper;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class AlertTransactionRepositoryImplExceptionTest {

    @InjectMocks
    private ServiceRepositoryImplement repositoryImpl;
    @Mock
    private ServiceRepository repository;
    @Spy
    private ServiceMapper mapper = Mappers.getMapper(ServiceMapper.class);
    private static final LocalDateTime NOW = LocalDateTime.now();
    @Mock
    private TimeFactory timeFactory;

    private final Service service = new Service();
    private final ServiceData serviceData = new ServiceData();

    @BeforeEach
    public void init() {
        service.setId(0);
        service.setName("EMAIL BASE");
        service.setIdState(1);
        service.setCreationUser("User");
        serviceData.setId(0);
        serviceData.setName("EMAIL BASE");
        serviceData.setIdState(1);
        serviceData.setCreationUser("User");
    }


    @Test
    public void findServiceByIdWithException() {
        when(repository.findById(anyInt()))
                .thenReturn(Mono.error(RuntimeException::new));
        repositoryImpl.findServiceById(service.getId())
                .as(StepVerifier::create)
                .expectError(TechnicalException.class)
                .verify();
    }

    @Test
    public void saveServiceWithException() {
        when(timeFactory.now()).thenReturn(NOW);
        when(repository.save(any()))
                .thenReturn(Mono.error(RuntimeException::new));
        repositoryImpl.saveService(service)
                .as(StepVerifier::create)
                .expectError(TechnicalException.class)
                .verify();
    }

    @Test
    public void updateServiceWithException() {
        when(repository.save(any()))
                .thenReturn(Mono.error(RuntimeException::new));
        when(repository.findById(anyInt()))
                .thenReturn(Mono.just(serviceData));
        repositoryImpl.updateService(service)
                .as(StepVerifier::create)
                .expectError(TechnicalException.class)
                .verify();
    }

    @Test
    public void deleteServiceWithException() {
        when(repository.deleteById(anyInt()))
                .thenReturn(Mono.error(RuntimeException::new));
        repositoryImpl.deleteServiceById(service.getId())
                .as(StepVerifier::create)
                .expectError(TechnicalException.class)
                .verify();
    }
}