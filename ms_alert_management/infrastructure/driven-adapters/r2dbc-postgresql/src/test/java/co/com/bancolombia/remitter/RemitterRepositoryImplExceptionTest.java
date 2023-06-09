package co.com.bancolombia.remitter;

import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.drivenadapters.TimeFactory;
import co.com.bancolombia.model.remitter.Remitter;
import co.com.bancolombia.remitter.data.RemitterData;
import co.com.bancolombia.remitter.data.RemitterMapper;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class RemitterRepositoryImplExceptionTest {

    @InjectMocks
    private RemitterRepositoryImplement repositoryImpl;
    @Mock
    private RemitterRepository repository;
    @Spy
    private RemitterMapper mapper = Mappers.getMapper(RemitterMapper.class);
    private static final LocalDateTime NOW = LocalDateTime.now();
    @Mock
    private TimeFactory timeFactory;

    private final Remitter remitter = new Remitter();
    private final RemitterData remitterData = new RemitterData();

    @BeforeEach
    public void init() {
        remitter.setId(0);
        remitter.setMail("mail@gmail.com");
        remitter.setState("Activo");

        remitterData.setId(0);
        remitterData.setMail("mail@gmail.com");
        remitterData.setState("Activo");
    }


    @Test
    void findRemitterByIdWithException() {
        when(repository.findById(anyInt()))
                .thenReturn(Mono.error(RuntimeException::new));
        repositoryImpl.findRemitterById(remitter.getId())
                .as(StepVerifier::create)
                .expectError(TechnicalException.class)
                .verify();
    }

    @Test
    void findAllRemitterWithException() {
        when(repository.findAll())
                .thenReturn(Flux.error(RuntimeException::new));
        repositoryImpl.findAll()
                .as(StepVerifier::create)
                .expectError(TechnicalException.class)
                .verify();
    }

    @Test
    void saveRemitterWithException() {
        when(timeFactory.now()).thenReturn(NOW);
        when(repository.save(any()))
                .thenReturn(Mono.error(RuntimeException::new));
        repositoryImpl.saveRemitter(remitter)
                .as(StepVerifier::create)
                .expectError(TechnicalException.class)
                .verify();
    }

    @Test
    void updateRemitterWithException() {
        when(repository.save(any()))
                .thenReturn(Mono.error(RuntimeException::new));
        when(repository.findById(anyInt()))
                .thenReturn(Mono.just(remitterData));
        repositoryImpl.updateRemitter(remitter)
                .as(StepVerifier::create)
                .expectError(TechnicalException.class)
                .verify();
    }

    @Test
    void deleteRemitterWithException() {
        when(repository.deleteById(anyInt()))
                .thenReturn(Mono.error(RuntimeException::new));
        repositoryImpl.deleteRemitterById(remitter.getId())
                .as(StepVerifier::create)
                .expectError(TechnicalException.class)
                .verify();
    }
}