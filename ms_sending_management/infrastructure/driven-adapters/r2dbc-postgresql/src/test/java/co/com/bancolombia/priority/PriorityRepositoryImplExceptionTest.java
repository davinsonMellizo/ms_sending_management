package co.com.bancolombia.priority;

import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.drivenadapters.TimeFactory;
import co.com.bancolombia.model.priority.Priority;
import co.com.bancolombia.priority.data.PriorityData;
import co.com.bancolombia.priority.data.PriorityMapper;
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

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class PriorityRepositoryImplExceptionTest {

    @InjectMocks
    private PriorityRepositoryImplement repositoryImpl;
    @Mock
    private PriorityRepository repository;
    @Spy
    private PriorityMapper mapper = Mappers.getMapper(PriorityMapper.class);
    private static final LocalDateTime NOW = LocalDateTime.now();
    @Mock
    private TimeFactory timeFactory;

    private final Priority priority = new Priority();
    private final PriorityData priorityData = new PriorityData();

    @BeforeEach
    public void init() {
        priority.setId(15);
        priority.setCode(1);
        priority.setDescription("description");
        priority.setIdProvider("HJK3");
    }

    @Test
    void findPriorityByIdWithException() {
        when(repository.findById(anyInt()))
                .thenReturn(Mono.error(RuntimeException::new));
        repositoryImpl.findPriorityById(priority.getId())
                .as(StepVerifier::create)
                .expectError(TechnicalException.class)
                .verify();
    }
}